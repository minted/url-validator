package com.minted.urlvalidator.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class URLValidatorService {

	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	public Map<String, String>  fxgJsonUrlValidator(List<String> urlList) throws InterruptedException, ExecutionException{
		
		final Map<String, String> resultMap = new HashMap<String, String>();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie" , "renderEndpoint=rubric");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		for (String url : urlList) {
			try {

				ResponseEntity<String> responseEntity =  restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			}catch(HttpServerErrorException serverErrorException) {
				resultMap.put(url, serverErrorException.getStatusCode().toString());
				System.out.println(url.concat(":").concat(serverErrorException.getStatusCode().toString()));

			}catch(HttpClientErrorException clientErrorException) {
				resultMap.put(url, clientErrorException.getStatusCode().toString());
				System.out.println(url.concat(":").concat(clientErrorException.getStatusCode().toString()));
			}

		}

		return resultMap;
	}



}
