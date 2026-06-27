package com.o2o.interceptor;

import com.o2o.enums.HttpApiCode;
import com.o2o.exceptions.BusinessException;
import com.o2o.exceptions.ShopOperationException;
import com.o2o.util.ResponseResultWrap;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseResultWrap<Object> handleBusinessException(BusinessException e) {
        logger.warn("业务异常: {}", e.getMessage());
        HttpApiCode apiCode = e.getApiCode() == null ? HttpApiCode.FAIL : e.getApiCode();
        return ResponseResultWrap.fail(apiCode, e.getMessage());
    }

    @ExceptionHandler(ShopOperationException.class)
    public ResponseResultWrap<Object> handleShopOperationException(ShopOperationException e) {
        logger.warn("店铺业务异常: {}", e.getMessage());
        return ResponseResultWrap.fail(HttpApiCode.FAIL, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResultWrap<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + "：" + err.getDefaultMessage())
                .collect(Collectors.joining("；"));
        logger.warn("参数校验失败: {}", msg);
        return ResponseResultWrap.fail(msg);
    }

    @ExceptionHandler(BindException.class)
    public ResponseResultWrap<Object> handleBindException(BindException e) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + "：" + err.getDefaultMessage())
                .collect(Collectors.joining("；"));
        logger.warn("参数绑定失败: {}", msg);
        return ResponseResultWrap.fail(msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResultWrap<Object> handleConstraintViolationException(ConstraintViolationException e) {
        logger.warn("参数约束异常: {}", e.getMessage());
        return ResponseResultWrap.fail("请求参数不合法");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseResultWrap<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.warn("缺少请求参数: {}", e.getParameterName());
        return ResponseResultWrap.fail("缺少必要参数: " + e.getParameterName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseResultWrap<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.warn("请求体解析失败: {}", e.getMessage());
        return ResponseResultWrap.fail("请求体格式错误");
    }

    @ExceptionHandler(Exception.class)
    public ResponseResultWrap<Object> handleException(Exception e) {
        logger.error("系统异常", e);
        return ResponseResultWrap.getResultByHttpCode(HttpApiCode.FAIL, null);
    }
}
