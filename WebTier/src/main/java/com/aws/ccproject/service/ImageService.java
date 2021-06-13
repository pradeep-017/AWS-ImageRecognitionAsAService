package com.aws.ccproject.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	String uploadFile(MultipartFile multipartFile) throws IOException; // new

	void sendImageToQueue(String imageUrl, String fileName); // new

	String getFromHashorSQS(String imageName);

}
