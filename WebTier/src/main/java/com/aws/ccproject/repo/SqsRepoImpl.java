package com.aws.ccproject.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.aws.ccproject.config.AwsConfiguration;

@Repository
public class SqsRepoImpl implements SqsRepo {
	
	private static final Logger logger = LoggerFactory.getLogger(SqsRepoImpl.class);

	@Autowired
	private AwsConfiguration awsConfiguration;

	@Override
	public void deleteMessage(List<Message> messages, String queueName) {
		logger.info("Deleting message batch from the queue.");
		String queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
//		String messageReceiptHandle = message.getReceiptHandle();
//		DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueUrl, messageReceiptHandle);
		List<DeleteMessageBatchRequestEntry> batchEntries = new ArrayList<>();
		
		for(Message msg : messages) {
			DeleteMessageBatchRequestEntry entry = new DeleteMessageBatchRequestEntry(msg.getMessageId(), msg.getReceiptHandle());
			batchEntries.add(entry);
		}
		DeleteMessageBatchRequest batch = new DeleteMessageBatchRequest(queueUrl, batchEntries);
		awsConfiguration.awsSQS().deleteMessageBatch(batch);
	}

	@Override
	public CreateQueueResult createQueue(String queueName) {
		logger.info("Creating the queue.");
		CreateQueueResult createQueueResult = awsConfiguration.awsSQS().createQueue(queueName);
		return createQueueResult;
	}

	@Override
	public List<Message> receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout) {
		logger.info("Receiving the message batch from the queue.");
		String queueUrl = null;
		try {
			queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
		} catch (QueueDoesNotExistException queueDoesNotExistException) {
//			log.info("SQS queue is not present in list and is creating now with name: " + queueName);
			CreateQueueResult createQueueResult = this.createQueue(queueName);
			queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
		}
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		receiveMessageRequest.setMaxNumberOfMessages(10);
		receiveMessageRequest.setWaitTimeSeconds(waitTime);
		receiveMessageRequest.setVisibilityTimeout(visibilityTimeout);
		ReceiveMessageResult receiveMessageResult = awsConfiguration.awsSQS().receiveMessage(receiveMessageRequest);
		List<Message> messageList = receiveMessageResult.getMessages();
		return messageList.isEmpty()? null : messageList;
	}

	@Override
	public void sendMessage(String messageBody, String queueName, Integer delaySeconds) {
		logger.info("Sending the message into the queue:" + messageBody);
		//Sending the message into the queue:https://hemanth546s3input.s3.amazonaws.com/test_0.JPEG-images
		String queueUrl = null;
		try {
			queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
		} catch (QueueDoesNotExistException queueDoesNotExistException) {
			logger.info("SQS queue is not present in list and is creating now with name: " + queueName);
			CreateQueueResult createQueueResult = this.createQueue(queueName);
			queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
		}
		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl)
				.withMessageBody(messageBody).withDelaySeconds(delaySeconds);
		awsConfiguration.awsSQS().sendMessage(sendMessageRequest);

	}

	@Override
	public Integer getApproximateNumberOfMsgs(String queueName) {
		logger.info("Getting approximate number of messages.");
		String queueUrl = null;
		try {
			queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
		} catch (QueueDoesNotExistException queueDoesNotExistException) {
//			log.info("SQS queue is not present in list and is creating now with name: " + queueName);
			CreateQueueResult createQueueResult = this.createQueue(queueName);
			queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
		}
		List<String> attributeNames = new ArrayList<String>();
		attributeNames.add("ApproximateNumberOfMessages");
		GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest(queueUrl, attributeNames);
		Map<String, String> map = awsConfiguration.awsSQS().getQueueAttributes(getQueueAttributesRequest).getAttributes();
		String numberOfMessagesString = (String) map.get("ApproximateNumberOfMessages");
		Integer numberOfMessages = Integer.valueOf(numberOfMessagesString);
		return numberOfMessages;
	}

}
