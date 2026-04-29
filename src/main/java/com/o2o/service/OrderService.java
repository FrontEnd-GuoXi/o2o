package com.o2o.service;

import com.o2o.entity.Order;
import com.o2o.entity.OrderItem;
import com.o2o.entity.PersonInfo;
import com.o2o.dto.OrderDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderService {


    Map<String, Object> addOrder (OrderDTO orderDTO, PersonInfo userInfo);

    BigDecimal calcTheAmount (BigDecimal sum, BigDecimal item);

    Boolean inventoryDeduction (List<Long> orderIdList);

    Boolean inventoryRelease (List<Long> orderIdList);

    Boolean updateStatusOfMultipleOrder (List<Order> orderList);

    List<Order> queryOrderListByIds (List<Long> orderIdList);


}
