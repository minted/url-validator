package com.minted.urlvalidator.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Async
public class EndpointCall {
	
	@Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<ResponseEntity<String>> callUrl(String url, HttpEntity entity) {
        return CompletableFuture.completedFuture(restTemplate.exchange(url,HttpMethod.GET,entity, String.class));
    }

}
