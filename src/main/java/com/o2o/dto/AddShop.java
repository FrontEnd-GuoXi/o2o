package com.o2o.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddShop {

    private String shopId;

    @NotNull
    private String shopName;

    private String shopDesc;

    @NotNull
    private String shopAddr;

    @NotNull
    private String phone;


    @NotNull
    private Integer priority;

    @NotNull
    private Integer enableStatus;

    @NotNull
    private Integer area;


    @NotNull
    private Long categorySub;


}
