package com.aws.ccproject.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.Message;
import com.aws.ccproject.constants.Constants;

@Service
public class ListenerAndDispatchingServiceImpl implements ListenerAndDispatchingService {
	
	private static Logger logger = LoggerFactory.getLogger(ListenerAndDispatchingServiceImpl.class);

	@Autowired
	private SqsService sqsService;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private Ec2Service ec2Service;

	@Override
	public void generalMethod() {
		while (true) {
			List<Message> inputMessages = sqsService.receiveMessage(Constants.INPUT_SQS, 0, 15);
			if (inputMessages == null)
				break;
			
			for(Message message : inputMessages) {
				
				String imageName = message.getBody();
				logger.info("Message: " + message + ", Image Name: " + imageName);
				String predictedValue = sqsService.imageRecognitionOutput(imageName);
				if (predictedValue == null) {
					predictedValue = Constants.NO_PREDICTION;
				}
				logger.info(Constants.IMAGE_PREDICTED_VALUE + predictedValue);
				s3Service.putObject(imageName.substring(0, imageName.length() - 5), predictedValue);
				sqsService.sendMessage(imageName + ":" + predictedValue, Constants.OUTPUT_SQS, 0);
			}
			
			sqsService.deleteMessageBatch(inputMessages, Constants.INPUT_SQS);
		}
//		ec2Service.endInstance();
	}

}
