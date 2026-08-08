package com.o2o.enums;

import java.util.Objects;

public enum PlatformTypeEnum {

    CONSUMER("consumer", "用户端"),
    MANAGER("manager", "管理端");

    /** 用于 @Pattern 校验的正则 */
    public static final String REGEX = "consumer|manager";

    private final String code;
    private final String desc;

    PlatformTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PlatformTypeEnum fromCode(String code) {
        for (PlatformTypeEnum item : values()) {
            if (Objects.equals(item.code, code)) {
                return item;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}