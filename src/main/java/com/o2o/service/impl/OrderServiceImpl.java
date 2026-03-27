package com.o2o.service.impl;

import com.o2o.dao.OrderDao;
import com.o2o.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class OrderServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    OrderDao orderDao;

    public Boolean addOrder (Order order) {

        int affectedRows = orderDao.addOrder(order);
        return affectedRows == 1;
    }


    private BigDecimal calcTheAmount () {

    }


}
