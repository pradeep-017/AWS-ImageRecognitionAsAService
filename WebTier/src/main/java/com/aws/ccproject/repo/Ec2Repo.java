package com.aws.ccproject.repo;

import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;

public interface Ec2Repo {
	DescribeInstanceStatusResult describeInstance(DescribeInstanceStatusRequest describeRequest);

	Integer createInstance(String imageId, Integer maxNumberOfInstances, Integer nameCount);

}
