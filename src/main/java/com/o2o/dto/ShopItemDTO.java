package com.o2o.dto;

import lombok.Data;

import java.util.List;

@Data
public class ShopItemDTO {

    private Long shopId;

    private List<ProductItemDTO> productList;

}
