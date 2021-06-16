package com.aws.ccproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.aws.ccproject.config.AppConfig;
import com.aws.ccproject.service.LoadBalanceService;

@SpringBootApplication
public class WebTierApplication {
	
	private static Logger logger = LoggerFactory.getLogger(WebTierApplication.class);
	
	// Main Class
	public static void main(String[] args) {
		SpringApplication.run(WebTierApplication.class, args);
		logger.info("WebTier is running!!!");
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		LoadBalanceService loadBalancingService = context.getBean(LoadBalanceService.class);
		loadBalancingService.scaleOut();
		
//		ImageService imgService = context.getBean(ImageService.class);
//		imgService.getFromHashorSQS(null);
		
		context.close();
	}

}
