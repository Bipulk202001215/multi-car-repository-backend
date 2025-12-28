package com.multicar.repository.demo.exception;

import com.multicar.repository.demo.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CustomRESTExceptionHandler extends org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorcode(ex.getErrorCode())
                .errormessage(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        log.error("Validation error: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorcode(ex.getErrorCode())
                .errormessage(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, WebRequest request) {
        log.error("Business error: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorcode(ex.getErrorCode())
                .errormessage(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("Illegal argument: {}", ex.getMessage(), ex);
        String errorCode = mapExceptionMessageToErrorCode(ex.getMessage());
        HttpStatus httpStatus = determineHttpStatus(errorCode);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorcode(errorCode)
                .errormessage(ex.getMessage())
                .status(httpStatus.value())
                .build();
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.error("Runtime exception: {}", ex.getMessage(), ex);
        String errorCode = mapExceptionMessageToErrorCode(ex.getMessage());
        HttpStatus httpStatus = determineHttpStatus(errorCode);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorcode(errorCode)
                .errormessage(ex.getMessage())
                .status(httpStatus.value())
                .build();
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        
        log.error("Validation error: {}", ex.getMessage(), ex);
        
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorcode(ErrorCode.VALIDATION_ERROR)
                .errormessage(errorMessage.isEmpty() ? "Validation failed" : errorMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorcode(ErrorCode.INTERNAL_SERVER_ERROR)
                .errormessage("An unexpected error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String mapExceptionMessageToErrorCode(String message) {
        if (message == null) {
            return ErrorCode.INTERNAL_SERVER_ERROR;
        }
        
        String lowerMessage = message.toLowerCase();
        
        // Company related
        if (lowerMessage.contains("company not found")) {
            return ErrorCode.COMPANY_NOT_FOUND;
        }
        
        // User related
        if (lowerMessage.contains("user not found")) {
            return ErrorCode.USER_NOT_FOUND;
        }
        if (lowerMessage.contains("invalid email") || lowerMessage.contains("invalid password") || 
            lowerMessage.contains("invalid credential")) {
            return ErrorCode.INVALID_CREDENTIALS;
        }
        if (lowerMessage.contains("email already exists")) {
            return ErrorCode.EMAIL_ALREADY_EXISTS;
        }
        if (lowerMessage.contains("password is required") || lowerMessage.contains("password required")) {
            return ErrorCode.PASSWORD_REQUIRED;
        }
        
        // Inventory related
        if (lowerMessage.contains("inventory not found")) {
            return ErrorCode.INVENTORY_NOT_FOUND;
        }
        if (lowerMessage.contains("insufficient stock")) {
            return ErrorCode.INSUFFICIENT_STOCK;
        }
        if (lowerMessage.contains("no items provided")) {
            return ErrorCode.NO_ITEMS_PROVIDED;
        }
        
        // Job related
        if (lowerMessage.contains("job") && lowerMessage.contains("not found")) {
            return ErrorCode.JOB_NOT_FOUND;
        }
        
        // Invoice related
        if (lowerMessage.contains("invoice not found")) {
            return ErrorCode.INVOICE_NOT_FOUND;
        }
        
        // Supplier related
        if (lowerMessage.contains("supplier not found")) {
            return ErrorCode.SUPPLIER_NOT_FOUND;
        }
        
        // UserRole related
        if (lowerMessage.contains("user role not found") || lowerMessage.contains("user-role not found")) {
            return ErrorCode.USER_ROLE_NOT_FOUND;
        }
        if (lowerMessage.contains("user-role combination already exists") || 
            lowerMessage.contains("user role combination already exists")) {
            return ErrorCode.USER_ROLE_COMBINATION_EXISTS;
        }
        
        // RolePermission related
        if (lowerMessage.contains("role permission not found") || lowerMessage.contains("role-permission not found")) {
            return ErrorCode.ROLE_PERMISSION_NOT_FOUND;
        }
        if (lowerMessage.contains("role-permission combination") || 
            lowerMessage.contains("role permission combination")) {
            return ErrorCode.ROLE_PERMISSION_COMBINATION_EXISTS;
        }
        
        // Role related
        if (lowerMessage.contains("role not found")) {
            return ErrorCode.ROLE_NOT_FOUND;
        }
        
        // Permission related
        if (lowerMessage.contains("permission not found")) {
            return ErrorCode.PERMISSION_NOT_FOUND;
        }
        
        // Default
        return ErrorCode.BAD_REQUEST;
    }

    private HttpStatus determineHttpStatus(String errorCode) {
        switch (errorCode) {
            case ErrorCode.COMPANY_NOT_FOUND:
            case ErrorCode.USER_NOT_FOUND:
            case ErrorCode.INVENTORY_NOT_FOUND:
            case ErrorCode.JOB_NOT_FOUND:
            case ErrorCode.INVOICE_NOT_FOUND:
            case ErrorCode.SUPPLIER_NOT_FOUND:
            case ErrorCode.USER_ROLE_NOT_FOUND:
            case ErrorCode.ROLE_PERMISSION_NOT_FOUND:
            case ErrorCode.ROLE_NOT_FOUND:
            case ErrorCode.PERMISSION_NOT_FOUND:
            case ErrorCode.NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            case ErrorCode.INVALID_CREDENTIALS:
            case ErrorCode.UNAUTHORIZED:
                return HttpStatus.UNAUTHORIZED;
            case ErrorCode.VALIDATION_ERROR:
            case ErrorCode.BAD_REQUEST:
            case ErrorCode.EMAIL_ALREADY_EXISTS:
            case ErrorCode.PASSWORD_REQUIRED:
            case ErrorCode.INSUFFICIENT_STOCK:
            case ErrorCode.NO_ITEMS_PROVIDED:
            case ErrorCode.USER_ROLE_COMBINATION_EXISTS:
            case ErrorCode.ROLE_PERMISSION_COMBINATION_EXISTS:
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}

