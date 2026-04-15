package com.o2o.service;

import com.o2o.entity.Order;
import com.o2o.entity.OrderItem;
import com.o2o.entity.PersonInfo;
import com.o2o.dto.OrderDTO;

import java.math.BigDecimal;

public interface OrderService {


    String addOrder (OrderDTO orderVO, PersonInfo userInfo);

    Boolean updateOrder (Order order);

    Boolean addOrderItem (OrderItem orderItem);

    Boolean updateOrderItem(OrderItem orderItem);

    BigDecimal calcTheAmount (BigDecimal sum, BigDecimal item);

    Boolean payForTheOrder (Long orderId);


}
