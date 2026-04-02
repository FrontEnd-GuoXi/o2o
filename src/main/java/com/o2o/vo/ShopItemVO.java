package com.o2o.vo;

import lombok.Data;

import java.util.List;

@Data
public class ShopItemVO {

    private Long shopId;

    private List<ProductItemVO> productList;

}
