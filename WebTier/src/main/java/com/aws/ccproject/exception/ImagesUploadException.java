package com.aws.ccproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Upload Failed")
public class ImagesUploadException extends Exception {
	private static final long serialVersionUID = 1L;

	public ImagesUploadException() {
		super("Images Upload failed!");
	}

}
