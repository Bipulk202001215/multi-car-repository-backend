package com.multicar.repository.demo.enums;

public enum ServiceType {
    PERIODIC("Regular periodic maintenance service"),
    REPAIR("General repair service"),
    AC("Air conditioning service and repair"),
    TIRES("Tire service and replacement"),
    PAINT("Paint and body work");

    private final String description;

    ServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}







