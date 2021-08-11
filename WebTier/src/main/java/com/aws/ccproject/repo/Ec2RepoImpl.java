package com.aws.ccproject.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.ec2.model.AmazonEC2Exception;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TagSpecification;
import com.aws.ccproject.config.AwsConfiguration;
import com.aws.ccproject.constants.Constants;
import com.aws.ccproject.constants.IntIdGenerator;

@Repository
public class Ec2RepoImpl implements Ec2Repo {

	private static final Logger logger = LoggerFactory.getLogger(Ec2RepoImpl.class);

	private static final String NAME = "Name";
	private static final String APP_INSTANCE = "AppTier-Instance";
	private static final String INSTANCE = "instance";
	private static final String OTHER_EXCEPTION = "Exception2: {}";

	@Autowired
	private AwsConfiguration awsConfiguration;

	@Override
	public Integer createInstance(String imageId, Integer maxNumberOfInstances, Integer nameCount) {
		List<String> securityGroupIds = new ArrayList<String>();
		Collection<TagSpecification> tagSpecifications = new ArrayList<TagSpecification>();
		TagSpecification tagSpecification = new TagSpecification();
		Collection<Tag> tags = new ArrayList<Tag>();
		Tag tag = new Tag();

		securityGroupIds.add(Constants.AWS_SECURITY_GROUP_ID1);
		securityGroupIds.add(Constants.AWS_SECURITY_GROUP_ID2);

		int nextVal = IntIdGenerator.generate();
		tag.setValue(APP_INSTANCE + "-" + nextVal);
		tag.setKey(NAME);
		tags.add(tag);
		tagSpecification.setResourceType(INSTANCE);
		tagSpecification.setTags(tags);
		tagSpecifications.add(tagSpecification);
		RunInstancesRequest runRequest = new RunInstancesRequest(imageId, 1, 1);
		runRequest.setInstanceType(InstanceType.T2Micro);
		runRequest.setSecurityGroupIds(securityGroupIds);
		runRequest.setTagSpecifications(tagSpecifications);
		try {
			awsConfiguration.awsEC2().runInstances(runRequest);
		} catch (AmazonEC2Exception amzEc2Exp) {
			logger.info("Creation of EC2 instance failed: " + amzEc2Exp.getErrorMessage());
		} catch (Exception e) {
			logger.info(OTHER_EXCEPTION, e.getMessage());
		}

		return nameCount;
	}

	@Override
	public DescribeInstanceStatusResult describeInstance(DescribeInstanceStatusRequest describeRequest) {
		return awsConfiguration.awsEC2().describeInstanceStatus(describeRequest);
	}

	@Override
	public DescribeInstancesResult describeInstances(DescribeInstancesRequest describeInstancesRequest) {
		return awsConfiguration.awsEC2().describeInstances(describeInstancesRequest);
	}

}
