package com.debijenkorf.elnazfaghihiimageServiceassignment.service.impl;

import com.debijenkorf.elnazfaghihiimageServiceassignment.dto.ImageDTO;
import com.debijenkorf.elnazfaghihiimageServiceassignment.enums.PredefinedTypeName;
import com.debijenkorf.elnazfaghihiimageServiceassignment.enums.ScaleType;
import com.debijenkorf.elnazfaghihiimageServiceassignment.service.ImageResizer;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class DefaultImageResizer implements ImageResizer {

    @Override
    public BufferedImage resize(ImageDTO imageDTO) {
        BufferedImage resizeFromCenter = null;
        PredefinedTypeName predefinedTypeName = imageDTO.getPredefinedTypeName();
        BufferedImage bufferSourceImage = imageDTO.getBufferedImage();
        if (!isAspectRatioFixed(predefinedTypeName, bufferSourceImage)) {
            if (predefinedTypeName.getScaleType() == ScaleType.SKEW) {
                resizeFromCenter = resizeMe(bufferSourceImage, predefinedTypeName.getWidth(), predefinedTypeName.getHeight());

            } else if (predefinedTypeName.getScaleType() == ScaleType.FILL) {
                resizeFromCenter = resizeToSquare(bufferSourceImage, predefinedTypeName, true, Color.decode(predefinedTypeName.getFillColor()), false);

            } else if (predefinedTypeName.getScaleType() == ScaleType.CROP) {
                resizeFromCenter = resizeFromCenter(bufferSourceImage, predefinedTypeName.getWidth(), predefinedTypeName.getHeight());
            } else {
                resizeFromCenter = resizeByCompatibleAspectRatio(bufferSourceImage, predefinedTypeName);
            }
        }

        return resizeFromCenter;
    }

    //check if original aspect is the same as
    private boolean isAspectRatioFixed(PredefinedTypeName predefinedTypeName, BufferedImage bufferImage) {
        return predefinedTypeName.getWidth() / predefinedTypeName.getHeight() == bufferImage.getWidth() / bufferImage.getHeight();
    }

    // resize image from center (cropped image), given size must be smaller than original file size
    private BufferedImage resizeFromCenter(BufferedImage bufferImage, int width, int height) {
        if (bufferImage.getHeight() < height || bufferImage.getWidth() < width) {
            // image is smaller than resize values
            System.out.println("Image is smaller than expected!");
            return null;
        }

        int xPos = (bufferImage.getWidth() - width) / 2;
        int yPos = (bufferImage.getHeight() - height) / 2;
        return bufferImage.getSubimage(xPos, yPos, width, height);
    }

    private BufferedImage resizeToSquare(BufferedImage bufferImage, PredefinedTypeName predefinedTypeName, boolean fullImageInSquare, Color backgroundColor, boolean enableTransparency) {
        if (fullImageInSquare) {
            BufferedImage img = null;
            int xPos = 0;
            int yPos = 0;
            if (bufferImage.getWidth() > bufferImage.getHeight()) {
                img = resizeToFixedWidthWithAspectRatio(bufferImage, predefinedTypeName.getWidth());
                yPos = (predefinedTypeName.getHeight() - img.getHeight()) / 2;
            } else if (bufferImage.getHeight() > bufferImage.getWidth()) {
                img = resizeToFixedHeightWithAspectRatio(bufferImage, predefinedTypeName.getHeight());
                xPos = (predefinedTypeName.getWidth() - img.getWidth()) / 2;
            }
            if (img != null) {
                boolean isTransparency = (backgroundColor == null && enableTransparency);
                BufferedImage squareImage = new BufferedImage(predefinedTypeName.getWidth(), predefinedTypeName.getHeight(), (isTransparency) ? BufferedImage.TYPE_INT_ARGB : img.getType());
                Graphics2D g2d = squareImage.createGraphics();
                if (!isTransparency) {
                    g2d.setPaint(backgroundColor);
                    g2d.fillRect(0, 0, predefinedTypeName.getWidth(), predefinedTypeName.getHeight());
                }
                g2d.drawImage(img.getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_SMOOTH), xPos, yPos, null);
                g2d.dispose();
                return squareImage;
            }
            return null;
        }
        // get a square from the center of an image
        Rectangle imgDimension = getSquareDimension(bufferImage);
        // crop image of square dimension
        BufferedImage croppedImg = bufferImage.getSubimage(imgDimension.x, imgDimension.y, imgDimension.width, imgDimension.height);
        // reduce the size of the square as needed & return
        return resizeMe(croppedImg, predefinedTypeName.getWidth(), predefinedTypeName.getHeight());
    }

    private Rectangle getSquareDimension(BufferedImage bufferImage) {
        // default width & height from image
        int imgW = bufferImage.getWidth();
        int imgH = bufferImage.getHeight();
        // default starting point is from 0,0
        int startX = 0;
        int startY = 0;
        // default widht, height from image
        int newW = imgW;
        int newH = imgH;
        // consider smallest size for square
        if (imgW > imgH) {
            newW = imgH;
            // if width is more then X needs to move
            startX = (int) Math.ceil((imgW - newW) / 2);
        } else if (imgW < imgH) {
            newH = imgW;
            // if height is more then Y needs to move
            startY = (int) Math.ceil((imgH - newH) / 2);
        }
        return new Rectangle(startX, startY, newW, newH);
    }


    private BufferedImage resizeMe(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, img.getType());
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    private BufferedImage resizeToFixedWidthWithAspectRatio(BufferedImage bufferImage, double maxWidth) {
        int scaledHeight = (int) Math.ceil((maxWidth * bufferImage.getHeight()) / bufferImage.getWidth());
        return resizeMe(bufferImage, (int) maxWidth, scaledHeight);
    }

    private BufferedImage resizeToFixedHeightWithAspectRatio(BufferedImage bufferImage, double maxHeight) {
        int scaledWidth = (int) Math.ceil((maxHeight * bufferImage.getWidth()) / bufferImage.getHeight());
        return resizeMe(bufferImage, scaledWidth, (int) maxHeight);
    }

    private BufferedImage resizeByCompatibleAspectRatio(BufferedImage sourceImage, PredefinedTypeName predefinedTypeName) {
        BufferedImage resizedImage = this.calculateAspectRatio(sourceImage, predefinedTypeName);
        int requestedWidth = predefinedTypeName.getWidth();
        int requestedHeight = predefinedTypeName.getHeight();
        BufferedImage newImage = new BufferedImage(requestedWidth, requestedHeight, sourceImage.getType());
        Graphics2D g1 = resizedImage.createGraphics();
        g1.drawImage(sourceImage, 0, 0, requestedWidth, requestedHeight, null);
        g1.dispose();

        int imageSpaceWidth = (requestedWidth - resizedImage.getWidth()) / 2;
        int imageSpaceHeight = (requestedHeight - resizedImage.getHeight()) / 2;

        Graphics2D g2 = newImage.createGraphics();
        g2.drawImage(newImage, 0, 0, requestedWidth, requestedHeight, null);
        g2.drawImage(resizedImage, imageSpaceWidth, imageSpaceHeight, null);
        g2.dispose();

        return newImage;
    }

    private BufferedImage calculateAspectRatio(BufferedImage sourceImage, PredefinedTypeName predefinedTypeName) {

        int requestedWidth = predefinedTypeName.getWidth();
        int requestedHeight = predefinedTypeName.getHeight();
        int originWidth = sourceImage.getWidth();
        int originHeight = sourceImage.getHeight();
        int newWidth = originWidth;
        int newHeight = originHeight;

        if (originWidth > requestedWidth) {

            newWidth = requestedWidth;
            newHeight = (requestedWidth * originHeight) / originWidth;
        }

        if (newHeight > requestedHeight) {

            newHeight = requestedHeight;
            newWidth = (newHeight * originWidth) / originHeight;
        }

        return new BufferedImage(newWidth, newHeight, sourceImage.getType());
    }

}
