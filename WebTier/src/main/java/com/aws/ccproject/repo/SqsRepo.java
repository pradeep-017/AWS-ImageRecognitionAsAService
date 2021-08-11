package com.aws.ccproject.repo;

import java.util.List;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;

public interface SqsRepo {

	CreateQueueResult createQueue(String queueName);

	List<Message> receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout);

	void sendMessage(String messageBody, String queueName, Integer delaySeconds);
	
	Integer getApproximateNumberOfMsgs(String queueName);

	void deleteMessage(List<Message> message, String queueName);

}
