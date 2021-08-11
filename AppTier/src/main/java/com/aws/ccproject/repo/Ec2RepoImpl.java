package com.aws.ccproject.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.util.EC2MetadataUtils;
import com.aws.ccproject.config.AwsConfiguration;
import com.aws.ccproject.constants.Constants;

@Repository
public class Ec2RepoImpl implements Ec2Repo {
	
	private static Logger logger = LoggerFactory.getLogger(Ec2RepoImpl.class);

	@Autowired
	private AwsConfiguration awsConfiguration;

	public void endInstance() {
		String currentEc2Id = EC2MetadataUtils.getInstanceId();
//		logger.info(Constants.ENDING_INSTANCE + currentEc2Id);
//		
//		String cmd="sudo shutdown -h now";
//
//		Runtime run = Runtime.getRuntime();
//		try {
//			Process pr = run.exec(cmd);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.exit(0);
		
		
		if(currentEc2Id !=null) {
			logger.info(Constants.ENDING_INSTANCE + currentEc2Id);
			
//			StopInstancesRequest stopIR = new StopInstancesRequest().withInstanceIds(currentEc2Id);
//			awsConfiguration.awsEC2().stopInstances(stopIR);
			TerminateInstancesRequest request = new TerminateInstancesRequest().withInstanceIds(currentEc2Id);
			awsConfiguration.awsEC2().terminateInstances(request);
		}
	}

}
