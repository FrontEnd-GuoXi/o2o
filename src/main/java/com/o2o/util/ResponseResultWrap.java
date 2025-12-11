package com.o2o.util;

import com.o2o.enums.HttpApiCode;
import com.o2o.exceptions.BusinessException;

public class ResponseResultWrap<T> {
    private String msg;
    private T data;

    private String code;

    private ResponseResultWrap(String code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static ResponseResultWrap<Object> success() {
        HttpApiCode httpCode = HttpApiCode.SUCCESS;
        return new ResponseResultWrap<Object>(httpCode.getCode(), null, httpCode.getMsg());
    }

    public static <T> ResponseResultWrap<T> success(T data) {
        HttpApiCode httpCode = HttpApiCode.SUCCESS;
        return new ResponseResultWrap<T>(httpCode.getCode(), data, httpCode.getMsg());
    }

    public static <T> ResponseResultWrap<T> success(String msg) {
        HttpApiCode httpCode = HttpApiCode.SUCCESS;
        return new ResponseResultWrap<T>(httpCode.getCode(), null, msg);
    }

    public static <T> ResponseResultWrap<T> success(T data, String msg) {
        HttpApiCode httpCode = HttpApiCode.SUCCESS;
        return new ResponseResultWrap<T>(httpCode.getCode(), data, msg);
    }

    public static ResponseResultWrap<Object> fail() {
        HttpApiCode httpCode = HttpApiCode.FAIL;
        return new ResponseResultWrap<Object>(httpCode.getCode(), null, httpCode.getMsg());
    }

    public static <T> ResponseResultWrap<T> fail(T data) {
        HttpApiCode httpCode = HttpApiCode.FAIL;
        return new ResponseResultWrap<T>(httpCode.getCode(), data, httpCode.getMsg());
    }

    public static <T> ResponseResultWrap<T> fail(String msg) {
        HttpApiCode httpCode = HttpApiCode.FAIL;
        return new ResponseResultWrap<T>(httpCode.getCode(), null, msg);
    }

    public static <T> ResponseResultWrap<T> fail(T data, String msg) {
        HttpApiCode httpCode = HttpApiCode.FAIL;
        return new ResponseResultWrap<T>(httpCode.getCode(), data, msg);
    }

    public static <T> ResponseResultWrap<T> getResultByHttpCode (HttpApiCode httpCode, T data) {
        return new ResponseResultWrap<>(httpCode.getCode(), data, httpCode.getMsg());
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
