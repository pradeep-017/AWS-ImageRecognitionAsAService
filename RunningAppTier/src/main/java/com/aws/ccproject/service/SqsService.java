package com.aws.ccproject.service;

import java.util.List;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;

public interface SqsService {
	
	public CreateQueueResult createQueue(String queueName);
	
//	void deleteMessage(Message message, String queueName);
	
	public List<Message> receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout);
	
	public void sendMessage(String messageBody, String queueName, Integer delaySeconds);

	public String imageRecognitionOutput(String imageName);

	void deleteMessageBatch(List<Message> messages, String queueName);
	
}
