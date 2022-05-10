package com.debijenkorf.elnazfaghihiimageServiceassignment.enums;

public enum ScaleType {
    CROP("crop"),
    FILL("fill"),
    SKEW("skew");

    private final String value;

    ScaleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
