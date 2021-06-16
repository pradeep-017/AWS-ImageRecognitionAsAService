package com.aws.ccproject.repo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aws.ccproject.config.AwsConfiguration;
import com.aws.ccproject.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class S3RepoImpl implements S3Repo {
	
	private static Logger logger = LoggerFactory.getLogger(S3RepoImpl.class);
	
	@Autowired
	private AwsConfiguration awsConfiguration;

	@Override
	public Bucket createBucket() {
		Bucket s3Bucket = null;
		if (awsConfiguration.awsS3().doesBucketExistV2(Constants.OUTPUT_S3)) {
			logger.info(Constants.GET_BUCKET + Constants.OUTPUT_S3);
			s3Bucket = getBucket();
		} else {
			logger.info(Constants.CREATE_BUCKET + Constants.OUTPUT_S3);
			s3Bucket = awsConfiguration.awsS3().createBucket(Constants.OUTPUT_S3);
		}

		return s3Bucket;
	}

	@Override
	public Bucket getBucket() {
		Bucket s3Bucket = null;
		List<Bucket> buckets = awsConfiguration.awsS3().listBuckets();
		for (Bucket b : buckets) {
			if (b.getName().equals(Constants.OUTPUT_S3))
				s3Bucket = b;
		}

		return s3Bucket;
	}

	@Override
	public void putObject(String key, String value) {
		logger.info(Constants.INSERTING_INTO_BUCKET);
		this.createBucket();
		@SuppressWarnings("serial")
		Map<String, String> predictionMap = new HashMap<String, String>() {
			{
				put(key, value);
			}
		};
		try {
			String prediction = new ObjectMapper().writeValueAsString(predictionMap);
			logger.info("Saving output prediction for image: " + key);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(prediction.length());
			InputStream stream = new ByteArrayInputStream(prediction.getBytes(StandardCharsets.UTF_8));
			final PutObjectRequest putObjectRequest = new PutObjectRequest(Constants.OUTPUT_S3, prediction, stream,
					meta);
			awsConfiguration.awsS3().putObject(putObjectRequest);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
