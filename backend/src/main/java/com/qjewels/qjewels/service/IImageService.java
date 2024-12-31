package com.qjewels.qjewels.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {
    String saveImageToStorage(String uploadDirectory, MultipartFile imageFile) throws IOException;
    byte[] getImage(String imageDirectory, String imageName) throws IOException;
    String deleteImage(String imageDirectory, String imageName) throws IOException;
}
