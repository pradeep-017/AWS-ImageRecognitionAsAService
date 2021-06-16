package com.aws.ccproject.constants;

import com.amazonaws.regions.Regions;

public class Constants {

//	public static final String AWS_ACCESS_KEY = "***";
	public static final String AWS_ACCESS_KEY = "***";

//	public static final String AWS_SECRET_KEY = "***";
	public static final String AWS_SECRET_KEY = "****";

	public static final Regions AWS_REGION = Regions.US_EAST_1;
	
	public static final String INPUT_S3 = "cc21prk-input-bucket";

	public static final String OUTPUT_S3 = "cc21prk-output-bucket";

	public static final String INPUT_SQS = "cc21prk-input-queue";

	public static final String OUTPUT_SQS = "cc21prk-output-queue";
	
	
	
	//Log messages for application
	
//	private static final String LISTENER_AND_DISPATCH_MAIN_ROUTINE ="Listener and dispatch main routine.";
	
	public static final String INSERTING_INTO_BUCKET = "Inserting Object Into S3 Bucket...";
	
	public static final String CREATE_BUCKET = "Creating S3 Bucket...";
	
	public static final String GET_BUCKET = "Getting existing S3 Bucket...";
	
	
	public static final String NO_PREDICTION ="No Prediction Value";
	
	public static final String IMAGE_PREDICTED_VALUE ="Image Predicted Value:";
	
	public static final String ENDING_INSTANCE="Ending the current instance: ";
	

}
