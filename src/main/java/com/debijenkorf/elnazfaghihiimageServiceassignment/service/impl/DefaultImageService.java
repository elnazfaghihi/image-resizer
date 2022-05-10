package com.debijenkorf.elnazfaghihiimageServiceassignment.service.impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.debijenkorf.elnazfaghihiimageServiceassignment.converter.FileNameConverter;
import com.debijenkorf.elnazfaghihiimageServiceassignment.dto.ImageDTO;
import com.debijenkorf.elnazfaghihiimageServiceassignment.enums.PredefinedTypeName;
import com.debijenkorf.elnazfaghihiimageServiceassignment.exception.ErrorCode;
import com.debijenkorf.elnazfaghihiimageServiceassignment.exception.runtime.SystemRuntimeException;
import com.debijenkorf.elnazfaghihiimageServiceassignment.service.ImageResizer;
import com.debijenkorf.elnazfaghihiimageServiceassignment.service.ImageService;
import com.debijenkorf.elnazfaghihiimageServiceassignment.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

@Service
public class DefaultImageService implements ImageService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultImageService.class);

    @Value("${source-root-url}")
    private String sourceRootUrl;

    private final FileNameConverter fileNameConverter;
    private final ImageResizer imageResizer;
    private final StorageService storageService;

    public DefaultImageService(FileNameConverter fileNameConverter, ImageResizer imageResizer, StorageService storageService) {
        this.fileNameConverter = fileNameConverter;
        this.imageResizer = imageResizer;
        this.storageService = storageService;
    }

    @Override
    public byte[] show(String typeName, String seoNam, String reference) {
        ImageDTO imageDTO = validateBeforeSave(typeName, reference);
        imageDTO.setReference(reference);
        String s3DirectoryPath = fileNameConverter.getOptimizedFileName(imageDTO.getReference(), imageDTO.getPredefinedTypeName());
        try {
            return storageService.downloadFile(s3DirectoryPath);
        } catch (IOException | AmazonS3Exception e) {
            logger.error("Optimized image does not exist on the bucket " + e);
            try {
                BufferedImage resized = imageResizer.resize(imageDTO);
                return storageService.uploadFile(resized, s3DirectoryPath, imageDTO.getPredefinedTypeName().getValue());
            } catch (IOException | AmazonS3Exception exception) {
                logger.warn("Could not store into s3 bucket " + exception);
            }
        }
        return null;
    }

    @Override
    public void flush(String typeName, String reference) {
        storageService.deleteFile(PredefinedTypeName.getByValue(typeName), reference);
    }

    private ImageDTO validateBeforeSave(String typeName, String reference) {
        String sourceImagePath = sourceRootUrl + reference;

        PredefinedTypeName predefinedTypeName = PredefinedTypeName.getByValue(typeName);

        if (predefinedTypeName == null) {
            throw new SystemRuntimeException(ErrorCode.NotFound, "INVALID_IMAGE_TYPE");
        }
        return new ImageDTO(sourceImagePath, predefinedTypeName, isImageExist(sourceImagePath));
    }

    public BufferedImage isImageExist(String url) {
        try {
            BufferedImage sourceImage = ImageIO.read(new URL(url));
            if (sourceImage != null) {
                return sourceImage;
            } else {
                throw new SystemRuntimeException(ErrorCode.NotFound, "SOURCE_IMAGE_NOT_EXIST");
            }
        } catch (IOException e) {
            throw new SystemRuntimeException(ErrorCode.NotFound, "SOURCE_IMAGE_NOT_EXIST");
        }
    }
}
