package com.multicar.repository.demo.enums;

public enum InventoryCategory {
    OEM("Original Equipment Manufacturer"),
    OES("Original Equipment Supplier"),
    LOCAL("Local/Aftermarket parts");

    private final String description;

    InventoryCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}








