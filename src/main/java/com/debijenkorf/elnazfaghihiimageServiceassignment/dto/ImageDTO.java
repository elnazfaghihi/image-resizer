package com.debijenkorf.elnazfaghihiimageServiceassignment.dto;

import com.debijenkorf.elnazfaghihiimageServiceassignment.enums.PredefinedTypeName;

import java.awt.image.BufferedImage;

public class ImageDTO {

    private String reference;
    private String sourceImagePath;
    private PredefinedTypeName predefinedTypeName;
    private BufferedImage bufferedImage;

    public ImageDTO(String sourceImagePath, PredefinedTypeName predefinedTypeName, BufferedImage bufferedImage) {
        this.sourceImagePath = sourceImagePath;
        this.predefinedTypeName = predefinedTypeName;
        this.bufferedImage = bufferedImage;
    }

    public ImageDTO() {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public PredefinedTypeName getPredefinedTypeName() {
        return predefinedTypeName;
    }

    public void setPredefinedTypeName(PredefinedTypeName predefinedTypeName) {
        this.predefinedTypeName = predefinedTypeName;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public String getSourceImagePath() {
        return sourceImagePath;
    }

    public void setSourceImagePath(String sourceImagePath) {
        this.sourceImagePath = sourceImagePath;
    }
}
