package com.multicar.repository.demo.enums;

public enum UserType {
    SUPER_ADMIN("Super administrator with system-wide access"),
    COMPANY_USER("Company-specific user with limited access");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}








