package com.debijenkorf.elnazfaghihiimageServiceassignment.enums;

public enum ImageType {
    JPG("JPG"),
    PNG("PNG");

    private final String value;

    ImageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
