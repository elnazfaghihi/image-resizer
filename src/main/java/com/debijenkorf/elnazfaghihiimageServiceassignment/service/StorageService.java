package com.debijenkorf.elnazfaghihiimageServiceassignment.service;

import com.debijenkorf.elnazfaghihiimageServiceassignment.enums.PredefinedTypeName;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface StorageService {
    void deleteFile(PredefinedTypeName predefinedTypeName, String reference);

    byte[] uploadFile(BufferedImage resized, String predefinedType, String directoryPath) throws IOException;

    byte[] downloadFile(String fileName) throws IOException;

}
