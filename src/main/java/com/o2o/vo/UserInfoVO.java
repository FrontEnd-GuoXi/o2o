package com.o2o.vo;

import lombok.Data;

@Data
public class UserInfoVO {
    private String userId;
    private String gender;

    // 1、顾客 2、店家 3、超级管理员
    private Integer userType;
    private String profileImg;
    private String name;

}
