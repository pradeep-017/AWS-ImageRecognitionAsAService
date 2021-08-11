package com.aws.ccproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aws.ccproject.constants.Constants;

@Service
public class LoadBalanceServiceImpl implements LoadBalanceService {

	private static final Logger logger = LoggerFactory.getLogger(LoadBalanceServiceImpl.class);

	@Autowired
	private SqsService sqsService;

	@Autowired
	private Ec2Service ec2Service;

	@Override
	public void scaleOut() {
		logger.info("ScaleOut started");
		Integer nameCount = 0;
		while (true) {
			Integer countOfRunningInstances = ec2Service.getNumOfInstances();
			Integer numberOfAppInstances = countOfRunningInstances - 1; //As 1 is webtier
			Integer numOfMsgs = sqsService.getApproxNumOfMsgs(Constants.INPUT_SQS);
//			numOfMsgs = numOfMsgs/Constants.MAX_REQUESTS_PER_INSTANCE;
			logger.info("Messages in InputSQS: " + numOfMsgs + ", Running Instances:" + countOfRunningInstances
					+ ", Running Apptier Instances:" + numberOfAppInstances);
			if (numOfMsgs > 0 && numOfMsgs > numberOfAppInstances) {
				Integer temp = Constants.MAX_RUNNING_INSTANCES - numberOfAppInstances;
				if (temp > 0) {
					logger.info("Start instances bcoz of temp: " + temp);
					nameCount += ec2Service.startInstances(1, nameCount);
					continue;
				}
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
