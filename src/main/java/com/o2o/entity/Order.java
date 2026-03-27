package com.o2o.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 */
@Data
public class Order {
    // 订单ID
    private Long orderId;
    // 买家信息
    private PersonInfo buyer;
    // 店铺信息
    private Shop shop;
    // 订单号
    private String orderNo;
    // 订单总金额
    private BigDecimal totalPrice;
    // 订单状态 0:未支付, 1:已支付, 2:已发货, 3:已完成, 4:已评价, -1:已取消
    private Integer orderStatus;
    // 创建时间
    private Date createTime;
    // 最后修改时间
    private Date lastEditTime;
    // 订单详情列表
    private List<OrderItem> orderItemList;


}
