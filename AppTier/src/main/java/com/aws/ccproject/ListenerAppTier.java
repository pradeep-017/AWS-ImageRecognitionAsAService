package com.aws.ccproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.aws.ccproject.config.AppConfig;
import com.aws.ccproject.service.ListenerAndDispatchingService;

@SpringBootApplication
public class ListenerAppTier {

	public static void main(String[] args) {
		SpringApplication.run(ListenerAppTier.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println("ListenerAppTier is running!");
		ListenerAndDispatchingService listenerAndDispatchingService = context
				.getBean(ListenerAndDispatchingService.class);
		listenerAndDispatchingService.generalFunction();
		context.close();
	}
}
