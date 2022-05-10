package com.debijenkorf.elnazfaghihiimageServiceassignment.controller;

import com.debijenkorf.elnazfaghihiimageServiceassignment.controller.routes.Routes;
import com.debijenkorf.elnazfaghihiimageServiceassignment.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Routes.IMAGE_CONTROLLER)
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/show/{typeName}/{seoName}")
    public ResponseEntity<byte[]> show(@PathVariable String typeName, @PathVariable(required = false) String seoName, @RequestParam(value = "reference") String reference) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageService.show(typeName, seoName, reference));

    }

    @GetMapping("/flush/{typeName}/")
    public void flush(@PathVariable String typeName, @RequestParam(value = "reference") String reference) {
        imageService.flush(typeName, reference);
    }
}
