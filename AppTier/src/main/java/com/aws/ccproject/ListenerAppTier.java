package com.aws.ccproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.aws.ccproject.config.AppConfig;
import com.aws.ccproject.service.ListenerAndDispatchingService;

@SpringBootApplication
public class ListenerAppTier {
	
	private static final Logger logger = LoggerFactory.getLogger(ListenerAppTier.class);

	public static void main(String[] args) {
		SpringApplication.run(ListenerAppTier.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		logger.info("ListenerAppTier is running!!!");
		ListenerAndDispatchingService listenerAndDispatchingService = context
				.getBean(ListenerAndDispatchingService.class);
		listenerAndDispatchingService.generalFunction();
		context.close();
	}
}
