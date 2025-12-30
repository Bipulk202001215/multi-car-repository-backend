package com.multicar.repository.demo.exception;

public class ValidationException extends RuntimeException {
    private final String errorCode;
    
    public ValidationException(String message) {
        super(message);
        this.errorCode = ErrorCode.VALIDATION_ERROR;
    }
    
    public ValidationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}



