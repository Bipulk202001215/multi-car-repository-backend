package com.multicar.repository.demo.exception;

public class ErrorCode {
    // Company related errors
    public static final String COMPANY_NOT_FOUND = "COMPANY_NOT_FOUND";
    
    // User related errors
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";
    public static final String PASSWORD_REQUIRED = "PASSWORD_REQUIRED";
    
    // Inventory related errors
    public static final String INVENTORY_NOT_FOUND = "INVENTORY_NOT_FOUND";
    public static final String INSUFFICIENT_STOCK = "INSUFFICIENT_STOCK";
    public static final String NO_ITEMS_PROVIDED = "NO_ITEMS_PROVIDED";
    
    // Job related errors
    public static final String JOB_NOT_FOUND = "JOB_NOT_FOUND";
    
    // Invoice related errors
    public static final String INVOICE_NOT_FOUND = "INVOICE_NOT_FOUND";
    
    // Supplier related errors
    public static final String SUPPLIER_NOT_FOUND = "SUPPLIER_NOT_FOUND";
    
    // UserRole related errors
    public static final String USER_ROLE_NOT_FOUND = "USER_ROLE_NOT_FOUND";
    public static final String USER_ROLE_COMBINATION_EXISTS = "USER_ROLE_COMBINATION_EXISTS";
    
    // RolePermission related errors
    public static final String ROLE_PERMISSION_NOT_FOUND = "ROLE_PERMISSION_NOT_FOUND";
    public static final String ROLE_PERMISSION_COMBINATION_EXISTS = "ROLE_PERMISSION_COMBINATION_EXISTS";
    
    // Role related errors
    public static final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";
    
    // Permission related errors
    public static final String PERMISSION_NOT_FOUND = "PERMISSION_NOT_FOUND";
    
    // General errors
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String NOT_FOUND = "NOT_FOUND";
    
    private ErrorCode() {
        // Utility class - prevent instantiation
    }
}

