package com.aws.ccproject.service;

import java.io.File;
import java.util.List;

public interface S3Service {
	
	List<String> getResponseResults();

	void uploadFileToS3Bucket(final String bucketName, final File file);

}
