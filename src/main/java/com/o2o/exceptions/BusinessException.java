package com.o2o.exceptions;

import com.o2o.enums.HttpApiCode;

public class BusinessException extends RuntimeException{

    HttpApiCode apiCode;

    public BusinessException(HttpApiCode apiCode, Throwable cause) {
        super(cause);
        this.apiCode = apiCode;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }


}
