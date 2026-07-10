package com.o2o.vo;


import java.math.BigDecimal;
import lombok.Data;

@Data
public class RecommendShopVO {
    private Long shopId;
    private String shopName;
    private String shopDesc;
    private String shopAddr;
    private String shopImg;
    private String shopCategoryId;
    private String shopCategoryName;
    private BigDecimal avgScore;
    private Integer evaluationCount;
}
