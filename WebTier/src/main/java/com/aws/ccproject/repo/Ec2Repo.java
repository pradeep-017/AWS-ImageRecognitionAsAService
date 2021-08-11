package com.aws.ccproject.repo;

import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;

public interface Ec2Repo {
	DescribeInstanceStatusResult describeInstance(DescribeInstanceStatusRequest describeRequest);

	Integer createInstance(String imageId, Integer maxNumberOfInstances, Integer nameCount);

	DescribeInstancesResult describeInstances(DescribeInstancesRequest describeInstancesRequest);

}
