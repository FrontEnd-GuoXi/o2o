package com.o2o.dao;

import com.o2o.entity.Order;
import com.o2o.entity.OrderItem;

public interface OrderDao {

    int addOrder (Order order);

    int updateOrder (Order order);

    int addOrderItem (OrderItem orderItem);

    int updateOrderItem(OrderItem orderItem);

    // 锁定库存
    int orderLocking (OrderItem orderItem);

    // 库存扣减
    int inventoryDeduction (OrderItem orderItem);

    // 超时释放
    int timeoutRelease (OrderItem orderItem);

    Order queryOrderById (Long orderId);



}
