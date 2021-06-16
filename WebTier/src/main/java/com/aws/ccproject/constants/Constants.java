package com.aws.ccproject.constants;

import com.amazonaws.regions.Regions;

public class Constants {

//	public static final String AWS_ACCESS_KEY = "****";
	public static final String AWS_ACCESS_KEY = "***";

//	public static final String AWS_SECRET_KEY = "****";
	public static final String AWS_SECRET_KEY = "***";

	public static final Regions AWS_REGION = Regions.US_EAST_1;

	public static final String INPUT_SQS = "cc21prk-input-queue";

	public static final String OUTPUT_SQS = "cc21prk-output-queue";

	public static final String INPUT_S3 = "cc21prk-input-bucket";

	public static final String OUTPUT_S3 = "cc21prk-output-bucket";

//	public static final String AWS_SECURITY_GROUP_ID1 = "sg-03936e1a";
	public static final String AWS_SECURITY_GROUP_ID1 = "sg-72eb856b";
	
//	public static final String AWS_SECURITY_GROUP_ID2 = "sg-0d69b8cbfa5493eb9";
	public static final String AWS_SECURITY_GROUP_ID2 = "sg-089a9248ec6672a6b";
	

	public static final Integer MAX_RUNNING_INSTANCES = 19;
	
	public static final Integer  MAX_REQUESTS_PER_INSTANCE = 5;

	public static final Integer EXISTING_INSTANCES = 1;

//	public static final String IMAGE_ID = "ami-0678b95b875d00f77";
	public static final String IMAGE_ID = "ami-0c419afb0f367ffd2"; 

	//
	public static final String INSERTING_INTO_BUCKET = "Inserting Object Into S3 Bucket...";

	public static final String CREATE_BUCKET = "Creating S3 Bucket...";

	public static final String GET_BUCKET = "Getting existing S3 Bucket...";

}
