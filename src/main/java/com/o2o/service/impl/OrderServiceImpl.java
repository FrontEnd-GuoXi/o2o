package com.o2o.service.impl;

import com.o2o.dao.OrderDao;
import com.o2o.dao.ProductInfoDao;
import com.o2o.entity.*;
import com.o2o.service.ProductInfoService;
import com.o2o.service.ShopService;
import com.o2o.util.Cls2Cls;
import com.o2o.util.SnowflakeIdGenerator;
import com.o2o.vo.OrderVO;
import com.o2o.vo.ProductItemVO;
import com.o2o.vo.ShopItemVO;
import com.o2o.vo.ShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl {

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

    @Transactional(rollbackFor = Exception.class)
    public String addOrder (OrderVO orderVO, PersonInfo userInfo) {
        List<ShopItemVO> shopList = orderVO.getShopList();

        BigDecimal totalPrice = shopList.stream().map(shopItemVO -> {
            Order order = new Order();
            order.setOrderId(snowflakeIdGenerator.nextId());
            order.setOrderStatus(0);
            Date currentDate = new Date();
            order.setCreateTime(currentDate);
            order.setLastEditTime(currentDate);
            order.setBuyer(userInfo);

            ShopVO shopVO = shopService.queryShopById(shopItemVO.getShopId(), userInfo.getUserId());
            Shop shop = Cls2Cls.shopVOToShop(shopVO, new Shop());
            order.setShop(shop);

            List<ProductItemVO>  productItemVOList = shopItemVO.getProductList();
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
                BigDecimal accPrice = this.calcTheAmount(order.getTotalPrice(), new BigDecimal(product.getNormalPrice()));
                order.setTotalPrice(accPrice);
            });

            orderDao.addOrder(order);
            return order;
        }).map(Order::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPrice.toString();
    }


    public BigDecimal calcTheAmount (BigDecimal sum, BigDecimal item) {
        return sum.add(item);
    }


}
