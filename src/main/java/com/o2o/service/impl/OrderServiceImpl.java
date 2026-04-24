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
    public String addOrder (OrderDTO orderDTO, PersonInfo userInfo) {
        try {
            List<ShopItemDTO> shopList = orderDTO.getShopList();
            List<Long> orderIdList = new ArrayList<>();

            BigDecimal totalPrice = shopList.stream().map(shopItemVO -> {
                Order order = new Order();
                order.setOrderId(snowflakeIdGenerator.nextId());
                order.setOrderNo(OrderCodeGenerator.generateOrderSn(userInfo.getUserId()));
                order.setOrderStatus(0);
                order.setTotalPrice(BigDecimal.ZERO);
                Date currentDate = new Date();
                order.setCreateTime(currentDate);
                order.setLastEditTime(currentDate);
                order.setBuyer(userInfo);

                ShopVO shopVO = shopService.queryShopById(shopItemVO.getShopId(), userInfo.getUserId());
                Shop shop = Cls2Cls.shopVOToShop(shopVO, new Shop());
                order.setShop(shop);
                // 先插入主订单，因为子订单有外键关联
                orderDao.addOrder(order);
                orderIdList.add(order.getOrderId());
                List<OrderItem> orderItemList = new ArrayList<>();
                List<ProductItemDTO>  productItemDTOList = shopItemVO.getProductList();
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
                orderDao.addOrderItem(orderItemList);
                int affectedRows = orderDao.orderLocking(orderItemList);
                if (affectedRows != orderItemList.size()) {
                    throw new BusinessException("库存锁定失败，可能部分商品库存不足");
                }
                order.setOrderItemList(orderItemList);
                // 更新主订单的总金额
                orderDao.updateOrder(order);
                return order;
            }).map(Order::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

            cartService.removeProductById(null, userInfo.getUserId().intValue());
            rabbitMQSender.send("o2o.direct.exchange", "order.create", orderIdList);
            return totalPrice.toString();
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
            List<Order> orderList = orderDao.queryOrderByIds(orderIdList);
            orderList.forEach(order -> {
                List<OrderItem> orderItemList = order.getOrderItemList();
                int orderItemListLen = orderItemList.size();
                int affectedRow = orderDao.inventoryDeduction(orderItemList);
                if (affectedRow != orderItemListLen) {
                    throw new BusinessException("受影响的行数为：" + affectedRow + "，实际订单项数量为：" + orderItemListLen);
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
            List<Order> orderList = orderDao.queryOrderByIds(orderIdList);
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




}
