package com.aws.ccproject.constants;

import com.amazonaws.regions.Regions;

public class Constants {

//	public static final String AWS_ACCESS_KEY = "****";
	public static final String AWS_ACCESS_KEY = "AKIAQJHC3S3ECRW3ZWI6";

//	public static final String AWS_SECRET_KEY = "****";
	public static final String AWS_SECRET_KEY = "dfHXG+LJkJhYZe8n1JBji4+HoVpu1DnxLj+C+Zfs";

	public static final Regions AWS_REGION = Regions.US_EAST_1;

	public static final String INPUT_SQS = "cc21cprk-input-queue";

	public static final String OUTPUT_SQS = "cc21cprk-output-queue";

	public static final String INPUT_S3 = "cc21cprk-input-bucket";

	public static final String OUTPUT_S3 = "cc21cprk-output-bucket";

//	public static final String AWS_SECURITY_GROUP_ID1 = "sg-03936e1a";
	public static final String AWS_SECURITY_GROUP_ID1 = "sg-2337743a";
	
//	public static final String AWS_SECURITY_GROUP_ID2 = "sg-0d69b8cbfa5493eb9";
	public static final String AWS_SECURITY_GROUP_ID2 = "sg-0ca880b2cd9d977a3";
	

	public static final Integer MAX_RUNNING_INSTANCES = 19;
	
	public static final Integer  MAX_REQUESTS_PER_INSTANCE = 5;

	public static final Integer EXISTING_INSTANCES = 1;

//	public static final String IMAGE_ID = "ami-0678b95b875d00f77";
	public static final String IMAGE_ID = "ami-0dd243525ac335e67";// "ami-0b0af0a0b094398d2"; 

	//
	public static final String INSERTING_INTO_BUCKET = "Inserting Object Into S3 Bucket...";

	public static final String CREATE_BUCKET = "Creating S3 Bucket...";

	public static final String GET_BUCKET = "Getting existing S3 Bucket...";

}
