package com.aws.ccproject.repo;

import java.util.List;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;

public interface SqsRepo {
	
	public CreateQueueResult createQueue(String queueName);
	
	public void sendMessage(String messageBody, String queueName, Integer delaySeconds);
	
	public List<Message> receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout);

	public String imageRecognitionProcess(String imageName);

	void deleteMessageBatch(List<Message> messages, String queueName);

}
