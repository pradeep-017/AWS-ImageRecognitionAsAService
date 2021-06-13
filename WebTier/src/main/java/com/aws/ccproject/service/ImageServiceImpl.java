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

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);
	
	private static final String FILE_NAME_DOES_NOT_EXISTS="The image doesn't has a name attached";
	
	private static Hashtable<String, String> hashTable = new Hashtable<String, String>();

	@Autowired
	private SqsService sqsService;

	@Autowired
	private S3Service s3Service;
	
	
	@Async
    @Override
    public String uploadFile(final MultipartFile multipartFile) throws IOException {
    	System.out.println("File upload in progress.");
    	String imageName = "";
    	System.out.println("Received multipartFile: " + multipartFile);
    	//Received multipartFile: org.springframework.web.multipart.support.
    	//StandardMultipartHttpServletRequest$StandardMultipartFile@5068a777
    	try {
    		File file =  convertMultiPartFileToFile(multipartFile);
    		System.out.println("Converted File: " + file);
    		//Converted File: test_0.JPEG
    		s3Service.uploadFileToS3Bucket(Constants.INPUT_S3,file);
    		System.out.println("File upload is completed."+multipartFile.getName());
//        	file.delete();
    		imageName = file.getName();
    		
    	} catch (final AmazonServiceException ex) {
            LOGGER.info("File upload is failed.");
            LOGGER.error("Error= {} while uploading file.", ex.getMessage());
        }
        //File upload is completed.images
        return imageName;
    }
	//doubt
	@Override
    public void sendImageToQueue(String imageName, String fileName) {
		//Need to change this
        sqsService.sendMessage(imageName, Constants.INPUT_SQS,0);
    }
	
	private File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        if(Objects.isNull(multipartFile.getOriginalFilename())) {
            throw new RuntimeException(FILE_NAME_DOES_NOT_EXISTS);
        }
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(file);
        try {
			outputStream.write(multipartFile.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
				List<Message> outputMessageFromQueue = sqsService.receiveMessage(Constants.OUTPUT_SQS, 15, 5);
				if( outputMessageFromQueue == null) {
					predictedName = hashTable.get(imageName);
					if(predictedName!=null)
						return predictedName;
					else
						continue;
				}
				
				for(Message outputMsg : outputMessageFromQueue) {
					
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
					if (imageNameInQueue.equals(imageName)) {
						sqsService.deleteMessage(outputMessageFromQueue, Constants.OUTPUT_SQS);
						return prediction;
					} else {
						hashTable.put(imageNameInQueue, prediction);
						sqsService.deleteMessage(outputMessageFromQueue, Constants.OUTPUT_SQS);
					}
				}
				
			} else {
//				hashTable.remove(imageName);
				return predictedName;
			}
		}
	}

}