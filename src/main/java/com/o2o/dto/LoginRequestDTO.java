package com.o2o.dto;

import com.o2o.enums.PlatformTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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


    /**
     * 平台类型，可选值：consumer（用户端）、manager（管理端）
     */
    @NotNull(message = "请传入平台名！")
    @Pattern(regexp = PlatformTypeEnum.REGEX, message = "平台类型只能是 consumer 或 manager")
    private String platform;

}
