package com.aws.ccproject.constants;

import com.amazonaws.regions.Regions;

public class Constants {

	public static final String AWS_ACCESS_KEY = "AKIATQRFWXLXYZPXVQR5";

	public static final String AWS_SECRET_KEY = "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";

	public static final Regions AWS_REGION = Regions.US_EAST_1;

	public static final String INPUT_SQS = "cc21-input-queue";

	public static final String OUTPUT_SQS = "cc21-output-queue";

	public static final String INPUT_S3 = "cc21-input-bucket";

	public static final String OUTPUT_S3 = "cc21-output-bucket";

	public static final String AWS_SECURITY_GROUP_ID = "sg-03936e1a";

	public static final Integer MAX_RUNNING_INSTANCES = 19;
	
	public static final Integer  MAX_REQUESTS_PER_INSTANCE = 5;

	public static final Integer EXISTING_INSTANCES = 1;

	public static final String IMAGE_ID = "ami-0678b95b875d00f77";

	//
	public static final String INSERTING_INTO_BUCKET = "Inserting Object Into S3 Bucket...";

	public static final String CREATE_BUCKET = "Creating S3 Bucket...";

	public static final String GET_BUCKET = "Getting existing S3 Bucket...";

}
