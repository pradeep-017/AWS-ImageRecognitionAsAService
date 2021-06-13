package com.aws.ccproject.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.util.EC2MetadataUtils;
import com.aws.ccproject.config.AwsConfiguration;
import com.aws.ccproject.constants.Constants;

@Repository
public class Ec2RepoImpl implements Ec2Repo {

	@Autowired
	private AwsConfiguration awsConfiguration;

	public void endInstance() {
		String currentEc2Id = EC2MetadataUtils.getInstanceId();
		if(currentEc2Id !=null) {
			System.out.println(Constants.ENDING_INSTANCE);
			TerminateInstancesRequest request = new TerminateInstancesRequest().withInstanceIds(currentEc2Id);
			awsConfiguration.awsEC2().terminateInstances(request);
		}
	}

}
