package com.multicar.repository.demo.enums;

public enum UserRole {
    ADMIN("All access, Reports, User management"),
    SERVICE_ADVISOR("Create/update job cards, Customer communication, Basic reports"),
    INVENTORY_MANAGER("Add/update stock, Purchase orders, Supplier management"),
    MECHANIC("Update job status, View assigned jobs, Parts requisition"),
    ACCOUNTANT("Generate invoices, Payment recording, Financial reports");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

