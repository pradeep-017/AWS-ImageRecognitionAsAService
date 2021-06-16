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
			Integer numberOfAppInstances = countOfRunningInstances - 1;
			Integer numOfMsgs = sqsService.getApproxNumOfMsgs(Constants.INPUT_SQS);
//			numOfMsgs = numOfMsgs/Constants.MAX_REQUESTS_PER_INSTANCE;
			logger.info("Msgs in InputQueue: " + numOfMsgs 
					+ ", Running Instances:" + countOfRunningInstances 
					+ ", Running App Instances:" + numberOfAppInstances);
			if (numOfMsgs > 0 && numOfMsgs > numberOfAppInstances) {
				Integer temp = Constants.MAX_RUNNING_INSTANCES - numberOfAppInstances;
				if (temp > 0) {
					Integer temp1 = numOfMsgs - numberOfAppInstances;
					logger.info("temp, temp1: " + temp + ", " + temp1);
					if (temp1 >= temp) {
						logger.info("Start instances with temp: " + temp);
						nameCount = ec2Service.startInstances(temp, nameCount);
					} else {
						logger.info("Start instances with temp1: " + temp1);
						nameCount = ec2Service.startInstances(temp1, nameCount);
					}
					try {
						Thread.sleep(30000); //we can increase it to 30sec
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					nameCount++;
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
