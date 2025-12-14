package com.o2o.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddShop {

    @NotNull
    private String ShopName;

    private String shopDesc;

    @NotNull
    private String shopAddr;

    @NotNull
    private String phone;

    private String shopImg;

    @NotNull
    private Integer priority;

    @NotNull
    private Integer enableStatus;

    @NotNull
    private String area;

    @NotNull
    private int categorySup;

    @NotNull
    private int categorySub;


}
