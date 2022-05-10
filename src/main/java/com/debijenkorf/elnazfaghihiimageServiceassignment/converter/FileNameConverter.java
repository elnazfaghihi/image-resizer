package com.debijenkorf.elnazfaghihiimageServiceassignment.converter;

import com.debijenkorf.elnazfaghihiimageServiceassignment.dto.ImageDTO;
import com.debijenkorf.elnazfaghihiimageServiceassignment.enums.PredefinedTypeName;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileNameConverter {

    private final String OLD_REPLACE = "/";
    private final String DIRECTORY_SEPARATOR = "/";

    public String getOptimizedFileName(String reference, PredefinedTypeName predefinedTypeName) {

       String originalImageFileName = checkForReplacement(reference);

        String fileWithoutExtension = FilenameUtils.removeExtension(originalImageFileName);

        return predefinedTypeName.getValue() + findDirectoryPath(fileWithoutExtension) + originalImageFileName;
    }

    String checkForReplacement(String originalImageFileName) {
        if (originalImageFileName.contains(OLD_REPLACE)) {
            return originalImageFileName.replace(OLD_REPLACE, "_");
        }
        return originalImageFileName;
    }

    String findDirectoryPath(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DIRECTORY_SEPARATOR);
        int length = fileName.length();
        if (length > 4) {
            stringBuilder.append(fileName, 0, 4);
            if (length > 8) {
                stringBuilder.append(DIRECTORY_SEPARATOR).append(fileName, 4, 8);
            }
        } else {
            stringBuilder.append(fileName);
        }
        return stringBuilder.append(DIRECTORY_SEPARATOR).toString();
    }
}
