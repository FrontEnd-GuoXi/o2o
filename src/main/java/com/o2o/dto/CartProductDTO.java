package com.o2o.dto;


import lombok.Data;

@Data
public class CartProductDTO {

    private int count;
    private int cartId;
    private Long productId;
    private String productName;
    private String imgAddr;
    private String normalPrice;
    private String promotionPrice;
    private int shopId;
    private String shopName;

}
