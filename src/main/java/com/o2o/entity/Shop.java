package com.o2o.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class Shop {
    private Long shopId;
    private String shopName;
    private String shopDesc;
    private String shopAddr;
    private String phone;
    private String shopImg;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime;
    private Integer enableStatus;
    private String advice;
    private Area area;
    private PersonInfo owner;
    private ShopCategory shopCategory;
    private BigDecimal avgScore;
    private Integer evaluationCount;
}
