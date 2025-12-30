package com.multicar.repository.demo.enums;

public enum PaymentMode {
    UPI("Unified Payments Interface"),
    CASH("Cash payment");

    private final String description;

    PaymentMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}






