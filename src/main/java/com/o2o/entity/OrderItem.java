package com.o2o.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情实体类
 */
@Data
public class OrderItem {
    // 订单详情ID
    private Long orderItemId;


    // 所属订单
    // 忽略这个字段，不参与 toString() 的生成
    @ToString.Exclude
    private Order order;
    // 商品信息
    private Product product;
    // 商品名称快照（防止商品删除或更名后历史数据丢失）
    private String productName;
    // 商品图片快照
    private String productImg;
    // 成交单价
    private BigDecimal unitPrice;
    // 购买数量
    private Integer quantity;
    // 该项总额
    private BigDecimal totalPrice;
    // 创建时间
    private Date createTime;

}
