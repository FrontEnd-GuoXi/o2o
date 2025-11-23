package com.o2o.util;

import com.o2o.enums.HttpApiCode;

public class ResponseResultUtil<T> {
    private String msg;
    private T data;

    private String code;

    private ResponseResultUtil(String code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static ResponseResultUtil<Object> success() {
        HttpApiCode httpCode = HttpApiCode.SUCCESS;
        return new ResponseResultUtil<Object>(httpCode.getCode(), null, httpCode.getMsg());
    }

    public static <T> ResponseResultUtil<T> success(T data) {
        HttpApiCode httpCode = HttpApiCode.SUCCESS;
        return new ResponseResultUtil<T>(httpCode.getCode(), data, httpCode.getMsg());
    }

    public static <T> ResponseResultUtil<T> success(String msg) {
        HttpApiCode httpCode = HttpApiCode.SUCCESS;
        return new ResponseResultUtil<T>(httpCode.getCode(), null, msg);
    }

    public static <T> ResponseResultUtil<T> success(T data, String msg) {
        HttpApiCode httpCode = HttpApiCode.SUCCESS;
        return new ResponseResultUtil<T>(httpCode.getCode(), data, msg);
    }

    public static ResponseResultUtil<Object> fail() {
        HttpApiCode httpCode = HttpApiCode.FAIL;
        return new ResponseResultUtil<Object>(httpCode.getCode(), null, httpCode.getMsg());
    }

    public static <T> ResponseResultUtil<T> fail(T data) {
        HttpApiCode httpCode = HttpApiCode.FAIL;
        return new ResponseResultUtil<T>(httpCode.getCode(), data, httpCode.getMsg());
    }

    public static <T> ResponseResultUtil<T> fail( String msg) {
        HttpApiCode httpCode = HttpApiCode.FAIL;
        return new ResponseResultUtil<T>(httpCode.getCode(), null, msg);
    }

    public static <T> ResponseResultUtil<T> fail(T data, String msg) {
        HttpApiCode httpCode = HttpApiCode.FAIL;
        return new ResponseResultUtil<T>(httpCode.getCode(), data, msg);
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public String getCode() {
        return code;
    }

}
