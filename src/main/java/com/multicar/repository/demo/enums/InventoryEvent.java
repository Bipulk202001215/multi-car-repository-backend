package com.multicar.repository.demo.enums;

public enum InventoryEvent {
    ADD("Add units to inventory"),
    SELL("Sell units from inventory");

    private final String description;

    InventoryEvent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}


