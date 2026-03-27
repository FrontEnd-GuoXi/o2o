package com.o2o.dao;

import com.o2o.entity.Order;
import com.o2o.entity.OrderItem;

public interface OrderDao {

    int addOrder (Order order);

    int updateOrder (Order order);

    int addOrderItem (OrderItem orderItem);

    int updateOrderItem(OrderItem orderItem);



}
