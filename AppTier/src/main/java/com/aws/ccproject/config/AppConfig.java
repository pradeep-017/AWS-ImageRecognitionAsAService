package com.aws.ccproject.config;

import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

import com.aws.ccproject.repo.Ec2Repo;
import com.aws.ccproject.repo.Ec2RepoImpl;
import com.aws.ccproject.repo.S3Repo;
import com.aws.ccproject.repo.S3RepoImpl;
import com.aws.ccproject.repo.SqsRepo;
import com.aws.ccproject.repo.SqsRepoImpl;
import com.aws.ccproject.service.Ec2Service;
import com.aws.ccproject.service.Ec2ServiceImpl;
import com.aws.ccproject.service.ListenerAndDispatchingService;
import com.aws.ccproject.service.ListenerAndDispatchingServiceImpl;
import com.aws.ccproject.service.S3Service;
import com.aws.ccproject.service.S3ServiceImpl;
import com.aws.ccproject.service.SqsService;
import com.aws.ccproject.service.SqsServiceImpl;

public class AppConfig {
	
	@Bean
	public AwsConfiguration awsConfiguration() {
		return new AwsConfiguration();
	}

	@Bean
	public S3Repo s3Repo() {
		return new S3RepoImpl();
	}

	@Bean
	public S3Service s3Service() {
		return new S3ServiceImpl();
	}
	
	@Bean
	public SqsService sqsService() {
		return new SqsServiceImpl();
	}

	@Bean
	public SqsRepo sqsRepo() {
		return new SqsRepoImpl();
	}
	
	@Bean
	public Ec2Repo ec2Repo() {
		return new Ec2RepoImpl();
	}

	@Bean
	public Ec2Service ec2Service() {
		return new Ec2ServiceImpl();
	}
	
	@Bean
	public ListenerAndDispatchingService listenerAndDispatchingService() {
		return new ListenerAndDispatchingServiceImpl();
	}

}
