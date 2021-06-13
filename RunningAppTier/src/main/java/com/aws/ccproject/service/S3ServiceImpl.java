package com.aws.ccproject.service;

import com.aws.ccproject.repo.S3Repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class S3ServiceImpl implements S3Service {

	@Autowired
	private S3Repo s3Repo;

	@Override
	public void putObject(String key, String value) {
		s3Repo.putObject(key, value);
	}

}
