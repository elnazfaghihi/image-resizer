package com.debijenkorf.elnazfaghihiimageServiceassignment.service;


import com.debijenkorf.elnazfaghihiimageServiceassignment.dto.ImageDTO;

import java.awt.image.BufferedImage;


public interface ImageResizer {

    BufferedImage resize(ImageDTO imageDTO);

}
