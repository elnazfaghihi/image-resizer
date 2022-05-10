package com.debijenkorf.elnazfaghihiimageServiceassignment.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.debijenkorf.elnazfaghihiimageServiceassignment.converter.FileNameConverter;
import com.debijenkorf.elnazfaghihiimageServiceassignment.enums.PredefinedTypeName;
import com.debijenkorf.elnazfaghihiimageServiceassignment.exception.ErrorCode;
import com.debijenkorf.elnazfaghihiimageServiceassignment.exception.runtime.SystemException;
import com.debijenkorf.elnazfaghihiimageServiceassignment.exception.runtime.SystemRuntimeException;
import com.debijenkorf.elnazfaghihiimageServiceassignment.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class DefaultStorageService implements StorageService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultStorageService.class);

    @Value("${application.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;
    private final FileNameConverter fileNameConverter;

    public DefaultStorageService(AmazonS3 s3Client, FileNameConverter fileNameConverter) {
        this.s3Client = s3Client;
        this.fileNameConverter = fileNameConverter;
    }


    public void deleteFile(PredefinedTypeName predefinedTypeName, String reference) {
        try {
            if ((predefinedTypeName == PredefinedTypeName.ORIGINAL)) {
                deleteByOriginalPredefinedType(reference);
            } else {
                s3Client.deleteObject(bucketName, fileNameConverter.getOptimizedFileName(reference, predefinedTypeName));
            }
        } catch (AmazonServiceException e) {
            throw new SystemException("COULD_NOT_DELETE_FILE");
        }
    }

    private void deleteByOriginalPredefinedType(String reference) {
        String[] s3DirPath = getS3DirPathOfTheSameReference(reference);

        if (!s3DirPath[0].isEmpty()) {
            DeleteObjectsRequest dor = new DeleteObjectsRequest(bucketName)
                    .withKeys(s3DirPath);
            s3Client.deleteObjects(dor);
        }
    }

    private String[] getS3DirPathOfTheSameReference(String reference) {
        String[] result = new String[PredefinedTypeName.values().length];
        int i = 0;
        for (PredefinedTypeName type : PredefinedTypeName.values()
        ) {
            try {
                String s3DirPath = fileNameConverter.getOptimizedFileName(reference, type);
                s3Client.getObject(bucketName, s3DirPath);
                result[i] = s3DirPath;
                i++;
            } catch (AmazonServiceException e) {
                logger.error("Could not find any files " + e);
            }
        }
        return result;
    }

    @Retryable(value = IOException.class, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public byte[] uploadFile(BufferedImage resized, String predefinedType, String directoryPath) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resized, predefinedType, os);
        byte[] buffer = os.toByteArray();
        InputStream is = new ByteArrayInputStream(buffer);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(buffer.length);

        s3Client.putObject(new PutObjectRequest(bucketName, directoryPath, is, meta));
        return buffer;
    }

    @Override
    public byte[] downloadFile(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        return IOUtils.toByteArray(inputStream);
    }

    @Recover
    public byte[] recover(IOException e) {
        logger.error("Could not store into s3 bucket after second retry" + e);
        return null;
    }
}
