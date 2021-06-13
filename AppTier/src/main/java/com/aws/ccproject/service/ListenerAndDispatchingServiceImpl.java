package com.aws.ccproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.Message;
import com.aws.ccproject.constants.Constants;

@Service
public class ListenerAndDispatchingServiceImpl implements ListenerAndDispatchingService {

	@Autowired
	private SqsService sqsService;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private Ec2Service ec2Service;

	@Override
	public void generalFunction() {
		while (true) {
			Message message = sqsService.receiveMessage(Constants.INPUT_SQS, 0, 15);
			if (message == null)
				break;
			String imageName = message.getBody();
			System.out.println("Message: " + message + ", Image Name: " + imageName);
			String predictedValue = sqsService.imageRecognitionOutput(imageName);
			if (predictedValue == null) {
				predictedValue = Constants.NO_PREDICTION;
			}
			System.out.println(Constants.IMAGE_PREDICTED_VALUE + predictedValue);
			s3Service.putObject(imageName.substring(0, imageName.length() - 5), predictedValue);
			sqsService.sendMessage(imageName + ":" + predictedValue, Constants.OUTPUT_SQS, 0);
			sqsService.deleteMessage(message, Constants.INPUT_SQS);
		}
		ec2Service.endInstance();
	}

}
