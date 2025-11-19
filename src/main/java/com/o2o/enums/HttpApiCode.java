package com.o2o.enums;

public enum HttpApiCode {
    SUCCESS("200", "请求成功"),
    FAIL("500", "请求失败");

    private final String code;
    private final String msg;


    HttpApiCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public String getCode () {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
