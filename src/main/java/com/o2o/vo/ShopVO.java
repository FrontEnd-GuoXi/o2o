package com.o2o.vo;

import lombok.Data;
import java.util.Date;

@Data
public class ShopVO {

    private String shopId;
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
    private String areaId;
    private String ownerId;
    private String shopCategoryId;
    private String shopCategoryParentId;

}
