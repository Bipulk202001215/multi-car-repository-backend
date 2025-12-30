package com.multicar.repository.demo.enums;

public enum GstSlab {
    FIVE_PERCENT("5% GST"),
    EIGHTEEN_PERCENT("18% GST"),
    TWENTY_EIGHT_PERCENT("28% GST");

    private final String description;

    GstSlab(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public double getPercentage() {
        return switch (this) {
            case FIVE_PERCENT -> 5.0;
            case EIGHTEEN_PERCENT -> 18.0;
            case TWENTY_EIGHT_PERCENT -> 28.0;
        };
    }
}





