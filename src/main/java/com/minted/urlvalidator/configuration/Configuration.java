package com.minted.urlvalidator.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import com.minted.urlvalidator.service.RejectedExecutionHandlerImpl;


@org.springframework.context.annotation.Configuration
public class Configuration {
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(200);
		executor.setQueueCapacity(2000);
		executor.setThreadNamePrefix("URLValidator-");
		executor.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
		executor.initialize();
		return executor;
	}


}
