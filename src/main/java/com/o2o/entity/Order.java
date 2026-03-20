package com.o2o.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 */
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PersonInfo getBuyer() {
        return buyer;
    }

    public void setBuyer(PersonInfo buyer) {
        this.buyer = buyer;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
