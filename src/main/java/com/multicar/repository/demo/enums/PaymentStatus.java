package com.multicar.repository.demo.enums;

public enum PaymentStatus {
    PENDING("Payment is pending"),
    COMPLETED("Payment has been completed");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}



