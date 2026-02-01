package com.multicar.repository.demo.enums;

public enum ServiceType {
    PERIODIC("Regular periodic maintenance service"),
    REPAIR("General repair service"),
    AC("Air conditioning service and repair"),
    TIRES("Tire service and replacement"),
    PAINT("Paint and body work"),
    BODYWORK("Bodywork and collision repair"),
    ELECTRICAL("Electrical system service and repair"),
    ENGINE("Engine service and repair"),
    OTHER("Other services");

    private final String description;

    ServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}









