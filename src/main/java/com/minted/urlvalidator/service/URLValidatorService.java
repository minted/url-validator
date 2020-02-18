
package com.minted.urlvalidator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class URLValidatorService {
	
	
	@Autowired
	private EndpointCall endpointCall;
	
	@SuppressWarnings("unchecked")
	public Map<String,String>  fxgJsonUrlValidator(List<String> urlList) throws InterruptedException, ExecutionException{

		Map<String,String> resultMap = new HashMap<String, String>();
		List<CompletableFuture<ResponseEntity<String>>> allFutures = new ArrayList<>();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie" , "renderEndpoint=rubric");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		
		for (String url : urlList) {
			allFutures.add(endpointCall.callUrl(url, entity));
		}
		CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();
		
		for (int i = 0; i < urlList.size(); i++) {
			try {
				resultMap.put(urlList.get(i), allFutures.get(i).get().getStatusCode().toString());
				System.out.println("URL: " + urlList.get(i) + ":   \t Status : " + allFutures.get(i).get().getStatusCode());
			}catch(HttpServerErrorException serverException) {
				resultMap.put(urlList.get(i), serverException.getStatusCode().toString());
				System.out.println("URL: " + urlList.get(i) + ":   \t Status : " + serverException.getStatusCode().toString());
			}catch(HttpClientErrorException clientException) {
				resultMap.put(urlList.get(i), clientException.getStatusCode().toString());
			}
		}
		return resultMap;
	}
}
