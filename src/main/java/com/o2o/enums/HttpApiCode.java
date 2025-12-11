package com.o2o.enums;

public enum HttpApiCode {
    SUCCESS("200", "请求成功"),
    FAIL("500", "请求失败"),
    TOKEN_EXPIRED("40101", "token过期"),
    INVALID_TOKEN("40102", "token无效"),
    UNAUTHORIZED("40103", "认证失败"),
    NOT_FOUND_USER("40401", "用户不存在");

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

    /**
     * 根据code查找对应的枚举常量
     * @param code 要查找的code值
     * @return 匹配的HttpApiCode枚举常量，如果找不到则返回null
     */
    public static HttpApiCode getByCode(String code) {
        // 遍历所有枚举常量
        for (HttpApiCode apiCode : HttpApiCode.values()) {
            // 比较code字段
            if (apiCode.getCode().equals(code)) {
                return apiCode; // 找到并返回
            }
        }
        return null; // 找不到则返回null
    }
}
