package com.aws.ccproject.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aws.ccproject.constants.Constants;

@Service
public class LoadBalanceServiceImpl implements LoadBalanceService {

	@Autowired
	private SqsService sqsService;

	@Autowired
	private Ec2Service ec2Service;

	@Override
	public void scaleOut() {
		System.out.println("ScaleOut started");
		Integer nameCount = 0;
		while (true) {
			Integer countOfRunningInstances = ec2Service.getNumOfInstances();
			Integer numberOfAppInstances = countOfRunningInstances - 1;
			Integer numOfMsgs = sqsService.getApproxNumOfMsgs(Constants.INPUT_SQS);
			System.out.println("Msgs in InputQueue: " + numOfMsgs 
					+ ", Running Instances:" + countOfRunningInstances 
					+ ", Running App Instances:" + numberOfAppInstances);
			if (numOfMsgs > 0 && numOfMsgs > numberOfAppInstances) {
				Integer temp = Constants.MAX_RUNNING_INSTANCES - numberOfAppInstances;
				if (temp > 0) {
					Integer temp1 = numOfMsgs - numberOfAppInstances;
					if (temp1 >= temp) {
						nameCount = ec2Service.startInstances(temp, nameCount);
					} else {
						nameCount = ec2Service.startInstances(temp1, nameCount);
					}
					try {
						Thread.sleep(20000); //we can increase it to 30sec
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					nameCount++;
				}
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
