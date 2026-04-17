package com.o2o.dao;

import com.o2o.entity.Order;
import com.o2o.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {

    int addOrder (Order order);

    int updateOrder (Order order);

    int addOrderItem (@Param("orderItemList") List<OrderItem> orderItemList);

    int updateOrderItem(@Param("orderItemList") List<OrderItem> orderItem);

    // 锁定库存
    int orderLocking (@Param("orderItemList") List<OrderItem> orderItem);

    // 库存扣减
    int inventoryDeduction (@Param("orderItemList") List<OrderItem> orderItem);

    // 超时释放
    int timeoutRelease (@Param("orderItemList") List<OrderItem> orderItem);

    List<Order> queryOrderByIds (@Param("orderIdList") List<Long> orderIdList);



}
