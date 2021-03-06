package com.aws.ccproject.service;

import com.aws.ccproject.repo.SqsRepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;

@Service
public class SqsServiceImpl implements SqsService {
	
	@Autowired
	private SqsRepo sqsRepo;
	
	@Override
	public void deleteMessageBatch(List<Message> messages, String queueName) {
		sqsRepo.deleteMessageBatch(messages, queueName);
	}
	
	@Override
	public CreateQueueResult createQueue(String queueName) {
		// TODO Auto-generated method stub
		CreateQueueResult createQueueResult = sqsRepo.createQueue(queueName);
		return createQueueResult;
	}
	
	@Override
	public String imageRecognitionOutput(String imageName) {
		String predictValue = sqsRepo.imageRecognitionProcess(imageName);
		
		return predictValue;
	}
	
	@Override
	public List<Message> receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout) {
		return sqsRepo.receiveMessage(queueName, waitTime, visibilityTimeout);
	}

	@Override
	public void sendMessage(String messageBody, String queueName, Integer delaySeconds) {
		sqsRepo.sendMessage(messageBody, queueName, delaySeconds);
	}
}
