package com.o2o.service.impl;

import com.o2o.dao.OrderDao;
import com.o2o.dao.ProductInfoDao;
import com.o2o.entity.*;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.CartService;
import com.o2o.service.OrderService;
import com.o2o.service.ProductInfoService;
import com.o2o.service.ShopService;
import com.o2o.util.Cls2Cls;
import com.o2o.util.OrderCodeGenerator;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
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
    ProductInfoDao productInfoDao;

    @Autowired
    CartService cartService;

    @Transactional(rollbackFor = Exception.class)
    public String addOrder (OrderDTO orderVO, PersonInfo userInfo) {
        try {
            List<ShopItemDTO> shopList = orderVO.getShopList();

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

                List<OrderItem> orderItemList = new ArrayList<>();
                List<ProductItemDTO>  productItemVOList = shopItemVO.getProductList();
                productItemVOList.forEach(productItemVO -> {
                    OrderItem orderItem = new OrderItem();
                    Long productId = productItemVO.getProductId();
                    Product product = productInfoService.getProductByProductId(productId);
                    Integer productInventory = product.getProductNumber() - productItemVO.getQuantity();
                    product.setProductNumber(productInventory);
                    product.setLastEditTime(new Date());
                    productInfoDao.updateProductInventory(product, productItemVO.getQuantity());

                    orderItem.setProduct(product);
                    orderItem.setOrder(order);
                    orderItem.setQuantity(productItemVO.getQuantity());
                    orderItem.setUnitPrice(new BigDecimal(product.getPromotionPrice()));
                    orderItem.setTotalPrice(orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                    orderItem.setCreateTime(new Date());

                    BigDecimal accPrice = this.calcTheAmount(order.getTotalPrice(), orderItem.getTotalPrice());
                    order.setTotalPrice(accPrice);

                    orderItemList.add(orderItem);
                    orderDao.addOrderItem(orderItem);
                });
                order.setOrderItemList(orderItemList);
                // 更新主订单的总金额
                orderDao.updateOrder(order);
                return order;
            }).map(Order::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

            cartService.removeProductById(null, userInfo.getUserId().intValue());

            return totalPrice.toString();
        } catch(BusinessException e) {
            logger.warn("订单生成失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }


    @Override
    public BigDecimal calcTheAmount (BigDecimal sum, BigDecimal item) {
        return sum.add(item);
    }

    @Override
    public Boolean updateOrder(Order order) {
        return orderDao.updateOrder(order) > 0;
    }

    @Override
    public Boolean addOrderItem(OrderItem orderItem) {
        return orderDao.addOrderItem(orderItem) > 0;
    }

    @Override
    public Boolean updateOrderItem(OrderItem orderItem) {
        return orderDao.updateOrderItem(orderItem) > 0;
    }
}
