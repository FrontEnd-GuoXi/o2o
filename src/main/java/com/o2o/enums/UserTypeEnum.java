package com.o2o.enums;

public enum UserTypeEnum {

    CUSTOMER(1, "顾客"),
    SHOP_OWNER(2, "店家"),
    SUPER_ADMIN(3, "超级管理员");

    private final int code;
    private final String desc;

    UserTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserTypeEnum fromCode(int code) {
        for (UserTypeEnum item : values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}