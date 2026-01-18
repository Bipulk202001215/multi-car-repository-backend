package com.multicar.repository.demo.enums;

public enum PaymentStatus {
    PENDING("Payment is pending"),
    PARTIAL("Payment has been completed"),
    PAID("Payment has paid");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}









