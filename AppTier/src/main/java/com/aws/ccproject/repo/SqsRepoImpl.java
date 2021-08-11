package com.aws.ccproject.repo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.aws.ccproject.config.AwsConfiguration;
import com.aws.ccproject.constants.Constants;

@Repository
public class SqsRepoImpl implements SqsRepo {
	
	private static Logger logger = LoggerFactory.getLogger(SqsRepoImpl.class);

	@Autowired
	private AwsConfiguration awsConfiguration;

	@Override
	public void deleteMessageBatch(List<Message> messages, String queueName) {
		logger.info("Deleting the message batch in the queue...");
		String queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
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
		logger.info("Creating the queue...");
		CreateQueueResult createQueueResult = awsConfiguration.awsSQS().createQueue(queueName);
		return createQueueResult;
	}

	public String imageRecognitionProcess(String imageName) {
		logger.info("Running the deep learning model...");
		String s3ImageUrl = "s3://" + Constants.INPUT_S3 + "/" + imageName;
		logger.info("s3ImageUrl: " + s3ImageUrl);

		GetObjectRequest request = new GetObjectRequest(Constants.INPUT_S3, imageName);
		S3Object object = awsConfiguration.awsS3().getObject(request);
		S3ObjectInputStream objectContent = object.getObjectContent();
		logger.info("s3ImageUrl: " + s3ImageUrl);
		try {
			logger.info("Downloading to location: ");
			IOUtils.copy(objectContent, new FileOutputStream("/home/ubuntu/classifier/" + imageName));
		} catch (FileNotFoundException e) {
			logger.info("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("IOException");
			e.printStackTrace();
		}

		String output = null;
		Process p;
		try {
			p = new ProcessBuilder("/bin/bash", "-c",
					"cd  /home/ubuntu/classifier/ && " + "python3 image_classification.py " + imageName).start();

			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			logger.info("ProcessBuilder: " + p);
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			logger.info("br: " + br);
			logger.info("strError: " + stdError);
			output = br.readLine();
			logger.info("termOutput: " + output);
			p.destroy();
		} catch (Exception e) {
			logger.info("Error while processing image recognition");
			e.printStackTrace();
		}
		return output.trim();
	}

	public String parseURL(String urlInput) {
		logger.info("Parsing the deep learning output.");
		String nameImage = null;
		String[] tokens = urlInput.split("/");
		for (String temp : tokens)
			nameImage = temp;
		return nameImage;
	}

	@Override
	public List<Message> receiveMessage(String sqsQueue, Integer waitTime, Integer visibilityTimeout) {
		logger.info("Receiving the message from the queue...");
		String queueUrl = awsConfiguration.awsSQS().getQueueUrl(sqsQueue).getQueueUrl();
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		receiveMessageRequest.setWaitTimeSeconds(waitTime);
		receiveMessageRequest.setVisibilityTimeout(visibilityTimeout);
		receiveMessageRequest.setMaxNumberOfMessages(1);
		ReceiveMessageResult receiveMessageResult = awsConfiguration.awsSQS().receiveMessage(receiveMessageRequest);
		List<Message> messageList = receiveMessageResult.getMessages();
		return messageList.isEmpty()? null : messageList;
	}

	@Override
	public void sendMessage(String messageBody, String queueName, Integer delaySeconds) {
		logger.info("Sending the message into the queue...");
		String queueUrl = null;
		try {
			queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
		} catch (QueueDoesNotExistException queueDoesNotExistException) {
			CreateQueueResult createQueueResult = this.createQueue(queueName);
		}
		queueUrl = awsConfiguration.awsSQS().getQueueUrl(queueName).getQueueUrl();
		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl)
				.withMessageBody(messageBody).withDelaySeconds(delaySeconds);
		awsConfiguration.awsSQS().sendMessage(sendMessageRequest);

	}

}
