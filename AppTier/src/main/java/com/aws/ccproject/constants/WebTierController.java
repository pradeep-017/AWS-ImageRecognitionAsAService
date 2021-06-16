package com.aws.ccproject.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebTierController {

	private static final Logger logger = LoggerFactory.getLogger(WebTierController.class);

	
	@GetMapping(value = "/")
	String index() {
		logger.info("Landing page...");
		return "index";
	}

	@GetMapping(value = "/upload")
	String uploadForm() {
		return "imageRecog";
	}
}
