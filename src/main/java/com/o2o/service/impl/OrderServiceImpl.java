package com.o2o.service.impl;

import com.o2o.dao.OrderDao;
import com.o2o.entity.*;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.CartService;
import com.o2o.service.OrderService;
import com.o2o.service.ProductInfoService;
import com.o2o.service.ShopService;
import com.o2o.util.Cls2Cls;
import com.o2o.util.OrderCodeGenerator;
import com.o2o.util.RabbitMQSender;
import com.o2o.util.SnowflakeIdGenerator;
import com.o2o.dto.OrderDTO;
import com.o2o.dto.ProductItemDTO;
import com.o2o.dto.ShopItemDTO;
import com.o2o.vo.ShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    ProductInfoService productInfoService;

    @Autowired
    ShopService shopService;

    @Autowired
    OrderDao orderDao;

    @Autowired
    private RabbitMQSender rabbitMQSender;


    @Autowired
    CartService cartService;

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addOrder (OrderDTO orderDTO, PersonInfo userInfo) {
        try {
            List<ShopItemDTO> shopList = orderDTO.getShopList();
            List<Long> orderIdList = new ArrayList<>();
            Map<String, Object> result = new HashMap<>();
            Map<String, String> shopIdMapOrderId = new HashMap<>();


            BigDecimal totalPrice = shopList.stream().map(shopItemDTO -> {
                Order order = new Order();
                order.setOrderId(snowflakeIdGenerator.nextId());
                order.setOrderNo(OrderCodeGenerator.generateOrderSn(userInfo.getUserId()));
                order.setOrderStatus(0);
                order.setTotalPrice(BigDecimal.ZERO);
                Date currentDate = new Date();
                order.setCreateTime(currentDate);
                order.setLastEditTime(currentDate);
                order.setBuyer(userInfo);

                shopIdMapOrderId.put(shopItemDTO.getShopId().toString(), order.getOrderId().toString());

                ShopVO shopVO = shopService.queryShopById(shopItemDTO.getShopId(), shopItemDTO.getUserId());
                Shop shop = Cls2Cls.shopVOToShop(shopVO, new Shop());
                order.setShop(shop);
                // 先插入主订单，因为子订单有外键关联
                orderDao.addOrder(order);
                orderIdList.add(order.getOrderId());
                List<OrderItem> orderItemList = new ArrayList<>();
                List<ProductItemDTO>  productItemDTOList = shopItemDTO.getProductList();
                productItemDTOList.forEach(ProductItemDTO -> {
                    OrderItem orderItem = new OrderItem();
                    Product product = productInfoService.getProductByProductId(ProductItemDTO.getProductId());
                    orderItem.setProduct(product);
                    orderItem.setOrder(order);
                    orderItem.setQuantity(ProductItemDTO.getQuantity());
                    orderItem.setUnitPrice(new BigDecimal(product.getPromotionPrice()));
                    orderItem.setTotalPrice(orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                    orderItem.setCreateTime(new Date());

                    BigDecimal accPrice = this.calcTheAmount(order.getTotalPrice(), orderItem.getTotalPrice());
                    order.setTotalPrice(accPrice);
                    orderItemList.add(orderItem);

                });

                // 1. 提取商品 ID 并去重排序
                List<Long> productIdList = orderItemList.stream()
                        .map(item -> item.getProduct().getProductId())
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());

                // 2. 显式锁定商品行，防止死锁（使用 SELECT ... FOR UPDATE）
                orderDao.selectProductsForUpdate(productIdList);

                // 3. 插入订单项（此时已持有 X 锁，不会再因为外键 S 锁升级导致死锁）
                orderDao.addOrderItem(orderItemList);

                // 对 orderItemList 进行排序，虽然上面已经按 ID 锁定了，但保持 orderLocking 的批量更新顺序一致也是好习惯
                orderItemList.sort(Comparator.comparing(item -> item.getProduct().getProductId()));
                int affectedRows = orderDao.orderLocking(orderItemList);
                if (affectedRows != orderItemList.size()) {
                    throw new BusinessException("库存锁定失败，可能部分商品库存不足");
                }
                order.setOrderItemList(orderItemList);
                // 更新主订单的总金额
                orderDao.updateOrder(order);
                List<Long> singleOrderId = new ArrayList<>();
                singleOrderId.add(order.getOrderId());
                rabbitMQSender.send("o2o.direct.exchange", "order.create", order.getOrderId());
                return order;
            }).map(Order::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

            cartService.removeProductById(null, userInfo.getUserId().intValue());
            result.put("shopIdMapOrderId", shopIdMapOrderId);
            result.put("totalPrice", totalPrice.toString());

            return result;
        } catch(BusinessException e) {
            logger.warn("订单生成失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean inventoryDeduction (List<Long> orderIdList) {
        try {
            List<Order> orderList = queryOrderListByIds(orderIdList);
            orderList.forEach(order -> {
                List<OrderItem> orderItemList = order.getOrderItemList();
                int orderItemListLen = orderItemList.size();
                int affectedRow = orderDao.inventoryDeduction(orderItemList);
                if (affectedRow != orderItemListLen) {
                    throw new BusinessException("受影响的行数为：" + affectedRow + "，实际订单项数量为：" + orderItemListLen);
                }
                order.setOrderStatus(1);
                int orderAffectedRow = orderDao.updateOrder(order);
                if (orderAffectedRow != 1) {
                    throw new BusinessException("更新订单状态失败，受影响的行数为" + affectedRow);
                }
            });

            return true;
        } catch (BusinessException e) {
            logger.warn("库存扣减失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error("库存扣减失败：{}", e.toString());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean inventoryRelease (List<Long> orderIdList) {
        try {
            List<Order> orderList = queryOrderListByIds(orderIdList);
            orderList.forEach(order -> {
                List<OrderItem> orderItemList = order.getOrderItemList();
                int orderItemListLen = orderItemList.size();
                int affectedRow = orderDao.timeoutRelease(orderItemList);
                if (affectedRow != orderItemListLen) {
                    throw new BusinessException("受影响的行数为：" + affectedRow + "，实际订单项数量为：" + orderItemListLen);
                }
            });

            return true;
        } catch (BusinessException e) {
            logger.warn("释放库存失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error("库存扣减失败：{}", e.toString());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatusOfMultipleOrder (List<Order> orderList) {
        try {
            int len = orderList.size();
            orderList.forEach(order -> {
                int affectedRow = orderDao.updateOrder(order);
                if (affectedRow != len) {
                    throw new BusinessException("受影响的行数为：" + affectedRow + "，实际订单项数量为：" + len);
                }
            });
            return true;
        } catch (BusinessException e) {
            logger.warn("订单更新失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error("订单更新失败：{}", e.toString());
            throw e;
        }
    }


    @Override
    public BigDecimal calcTheAmount (BigDecimal sum, BigDecimal item) {
        return sum.add(item);
    }

    public List<Order> queryOrderListByIds (List<Long> orderIdList) {
        if (orderIdList == null || orderIdList.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return orderDao.queryOrderByIds(orderIdList);
        } catch (BusinessException e) {
            logger.warn("订单列表查询失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error("订单列表查询失败：{}", e.toString());
            throw e;
        }
    }




}
