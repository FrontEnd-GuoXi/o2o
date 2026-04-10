package com.o2o.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {


    private List<ShopItemDTO>  shopList;
    private String token;


}
