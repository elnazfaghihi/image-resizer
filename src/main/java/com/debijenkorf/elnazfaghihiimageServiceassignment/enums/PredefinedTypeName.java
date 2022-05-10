package com.debijenkorf.elnazfaghihiimageServiceassignment.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum PredefinedTypeName {

    THUMBNAIL("thumbnail", 1280, 720, 90, ScaleType.CROP, ImageType.JPG, "#ffffff"),
    ORIGINAL("original");

    private final String value;
    private final int width;
    private final int height;
    private final int quality;
    private final ScaleType scaleType;
    private final ImageType imageType;
    private final String fillColor;

    PredefinedTypeName(String value, int width, int height, int quality, ScaleType scaleType, ImageType imageType, String fillColor) {
        this.value = value;
        this.width = width;
        this.height = height;
        this.quality = quality;
        this.scaleType = scaleType;
        this.imageType = imageType;
        this.fillColor = fillColor;
    }

    PredefinedTypeName(String value) {
        this.value = value;
        this.width = 0;
        this.height = 0;
        this.quality = 0;
        this.scaleType = null;
        this.imageType = null;
        this.fillColor = "";
    }

    public String getValue() {
        return value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getQuality() {
        return quality;
    }

    public ScaleType getScaleType() {
        return scaleType;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public String getFillColor() {
        return fillColor;
    }

    private static final Map<String, PredefinedTypeName> lookup = Arrays.stream(PredefinedTypeName.values())
            .collect(Collectors.toMap(PredefinedTypeName::getValue, sub -> sub));

    public static PredefinedTypeName getByValue(String value) {
        return lookup.get(value);
    }
}
