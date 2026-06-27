package com.o2o.exceptions;

import com.o2o.enums.HttpApiCode;

public class BusinessException extends RuntimeException {

    private final HttpApiCode apiCode;

    public BusinessException(String message) {
        super(message);
        this.apiCode = HttpApiCode.FAIL;
    }

    public BusinessException(HttpApiCode apiCode, String message) {
        super(message);
        this.apiCode = apiCode;
    }

    public BusinessException(HttpApiCode apiCode, String message, Throwable cause) {
        super(message, cause);
        this.apiCode = apiCode;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.apiCode = HttpApiCode.FAIL;
    }

    public HttpApiCode getApiCode() {
        return apiCode;
    }
}
