package com.o2o.dto;

import lombok.Data;

@Data
public class PersonInfoDTO {

    private Long userId;
    private String gender;

    //  '0：禁止使用，1：允许使用'
    private Integer enableStatus;

    // 1、顾客 2、店家 3、超级管理员
    private Integer userType;

}
