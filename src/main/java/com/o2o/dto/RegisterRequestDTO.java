package com.o2o.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {


    @NotNull(message = "账号不能为空！")
    @Size(min = 6, max=20, message = "账号长度在6-20字符之间。")
    private String identifier;             // 唯一标识：用户名、微信 openid、手机号等

    @NotNull(message = "密码不能为空！")
    @Size(min = 6, max = 20, message = "密码长度在6-20字符之间。")
    private String credential;             // 凭证：密码哈希值、access_token 等

    @NotNull(message = "名称不能为空！")
    @Size(min = 1, max = 50, message = "名称长度在1-50字符之间")
    private String name;

    private String profileImg;

    @NotNull(message = "请选择性别！")
    private String gender;


    // 1、顾客 2、店家 3、超级管理员
    @NotNull(message = "请选择用户类型！")
    private Integer userType;

}
