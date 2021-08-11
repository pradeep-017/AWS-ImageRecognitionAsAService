package com.aws.ccproject.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.Message;
import com.aws.ccproject.constants.Constants;

@Service
public class ImageServiceImpl implements ImageService {

	private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

	private static final String FILE_NAME_DOES_NOT_EXISTS = "The image doesn't has a name attached";

	private static Hashtable<String, String> hashTable = new Hashtable<String, String>();

	@Autowired
	private SqsService sqsService;

	@Autowired
	private S3Service s3Service;

	@Async
	@Override
	public String uploadFiles(final MultipartFile multipartFile) throws IOException {
		logger.info("File upload in progress.");
		String imageName = "";
		logger.info("Received multipartFile: " + multipartFile);
		try {
			File file = convertMultiPartFileToFile(multipartFile);
			logger.info("Converted File: " + file);
			s3Service.uploadFileToS3Bucket(Constants.INPUT_S3, file);
			logger.info("File upload is completed." + multipartFile.getName());
			imageName = file.getName();
		} catch (final AmazonServiceException ex) {
			logger.info("File upload is failed.");
			logger.error("Error= {} while uploading file.", ex.getMessage());
		}
		// Files upload is completed!
		return imageName;
	}

	@Override
	public void sendImageToQueue(String imageName, String fileName) {
		sqsService.sendMessage(imageName, Constants.INPUT_SQS, 0);
	}

	private File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
		if (Objects.isNull(multipartFile.getOriginalFilename())) {
			throw new RuntimeException(FILE_NAME_DOES_NOT_EXISTS);
		}
		File file = new File(multipartFile.getOriginalFilename());
		FileOutputStream outputStream = new FileOutputStream(file);
		try {
			outputStream.write(multipartFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			outputStream.close();
		}
		return file;
	}

	public String getFromHashorSQS(String imageName) {
		while (true) {
			String predictedName = hashTable.get(imageName);
			if (predictedName == null) {
				List<Message> outputMessageFromQueue = sqsService.receiveMessage(Constants.OUTPUT_SQS, 15, 15);
				logger.info("outputMessageFromQueue:" + outputMessageFromQueue);
				if (outputMessageFromQueue == null) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}

				for (Message outputMsg : outputMessageFromQueue) {

					String outputMessageBodyFromQueue = outputMsg.getBody();
					String[] tokens = outputMessageBodyFromQueue.split(":");
					Integer count = 0;
					String imageNameInQueue = null;
					String prediction = null;
					for (String string : tokens) {
						if (count == 0)
							imageNameInQueue = string;
						else
							prediction = string;
						count++;
					}
					hashTable.put(imageNameInQueue, prediction);
				}
				sqsService.deleteMessage(outputMessageFromQueue, Constants.OUTPUT_SQS);
				predictedName = hashTable.get(imageName);
				if (predictedName != null)
					return predictedName;

			} else {
				return predictedName;
			}
		}
	}

}
