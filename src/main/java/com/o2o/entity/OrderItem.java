package com.o2o.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情实体类
 */
public class OrderItem {
    // 订单详情ID
    private Long orderItemId;
    // 所属订单
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

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
