package com.aws.ccproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.aws.ccproject.config.AppConfig;
import com.aws.ccproject.service.ImageService;
import com.aws.ccproject.service.LoadBalanceService;

@SpringBootApplication
public class WebTierApplication {
	// Main Class
	public static void main(String[] args) {
		SpringApplication.run(WebTierApplication.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		LoadBalanceService loadBalancingService = context.getBean(LoadBalanceService.class);
		loadBalancingService.scaleOut();
		
//		ImageService imgService = context.getBean(ImageService.class);
//		imgService.getFromHashorSQS(null);
		
		context.close();
	}

}