package com.o2o.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotNull(message = "账号不能为空！")
    @Size(min = 6, max=20, message = "账号长度在6-20字符之间。")
    private String identifier;             // 唯一标识：用户名、微信 openid、手机号等

    @NotNull(message = "密码不能为空！")
    @Size(min = 6, max = 20, message = "密码长度在6-20字符之间。")
    private String credential;

}
