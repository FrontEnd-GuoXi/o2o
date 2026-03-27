package com.o2o.service.impl;

import com.o2o.entity.Order;
import com.o2o.entity.OrderItem;

public interface OrderService {


    Boolean addOrder (Order order);

    Boolean updateOrder (Order order);

    Boolean addOrderItem (OrderItem orderItem);

    Boolean updateOrderItem(OrderItem orderItem);


}
