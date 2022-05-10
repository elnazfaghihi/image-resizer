package com.debijenkorf.elnazfaghihiimageServiceassignment.service;

public interface ImageService {

    byte[] show(String typeName, String seoNam, String reference);

    void flush(String typeName,String reference);
}
