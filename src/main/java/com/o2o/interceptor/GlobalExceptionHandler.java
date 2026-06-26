package com.o2o.interceptor;

import com.o2o.exceptions.BusinessException;
import com.o2o.util.ResponseResultWrap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseResultWrap handleBusinessException(BusinessException e) {
        return ResponseResultWrap.fail(e.apiCode, e.getMessage());
    }
}
