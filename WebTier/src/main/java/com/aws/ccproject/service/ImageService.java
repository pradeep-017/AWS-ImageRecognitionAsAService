package com.aws.ccproject.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	String uploadFiles(MultipartFile multipartFile) throws IOException;

	void sendImageToQueue(String imageUrl, String fileName);

	String getFromHashorSQS(String imageName);

}
