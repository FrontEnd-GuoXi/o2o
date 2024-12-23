package com.o2o.enums;

public enum ShopStateEnum {

    CHECK(0, "审核中"), ILLEGALITY(-1, "非法店铺"),SUCCESS(1, "操作成功"),PASS(2, "通过认证"),INNER_ERROR(-1001,"内部系统错误"),
    NULL_SHOPID(-1002, "shopID为空"),NULL_SHOP(-1003,"shop信息为空");

    private int state;
    private String stateInfo;

    private ShopStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ShopStateEnum itemOf(int state) {
        for (ShopStateEnum item : values()) {
            if (item.getState() == state) {
                return item;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
