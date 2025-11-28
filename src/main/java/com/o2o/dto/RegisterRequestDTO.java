package com.o2o.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {


    private String identifier;             // 唯一标识：用户名、微信 openid、手机号等
    private String credential;             // 凭证：密码哈希值、access_token 等

    private String name;
    private String profileImg;
    private String gender;

    //  '0：禁止使用，1：允许使用'
    private Integer enableStatus;

    // 1、顾客 2、店家 3、超级管理员
    private Integer userType;





}
