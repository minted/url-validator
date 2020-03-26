
package com.minted.urlvalidator.service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.minted.urlvalidator.model.FxgJsonOutputObject;

@Service
public class URLValidatorService {
	
	Logger log = LoggerFactory.getLogger(URLValidatorService.class);
	
	@Autowired
	private EndpointCall endpointCall;
	
	private static final String CSV_SEPARATOR = "|";
	
	@SuppressWarnings("unchecked")
	public List<FxgJsonOutputObject> fxgJsonResponseComparator(List<String> urlList) throws InterruptedException, ExecutionException{
		
		 List<CompletableFuture<ResponseEntity<String>>> allRubricFutures = new ArrayList<>();
		
		 List<CompletableFuture<ResponseEntity<String>>> allScene7Futures = new ArrayList<>();
		
		String fxgurl = null;
		
	    List<FxgJsonOutputObject> fxgJsonOutputObjectList = new ArrayList<FxgJsonOutputObject>();
	    
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie" , "renderEndpoint=rubric");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		
		log.info("Started sending the requests to rubric endpoint");
		for(int i=0; i<(urlList.size()-1); i++) {
			allRubricFutures.add(endpointCall.callUrl(urlList.get(i), entity));
		}
		
		log.info("Started sending the requests to scene7 endpoint");
		for(int i=0; i<(urlList.size()-1); i++) {
			allScene7Futures.add(endpointCall.callUrl(urlList.get(i), null));
		}
		
		log.info("Consolidating the requests from scene7 and rubric and working on response from both");
		for (int i = 0; i < urlList.size()-1; i++) {
			try {
				fxgurl= urlList.get(i);
				 String rubricStatusCode = allRubricFutures.get(i).get().getStatusCode().toString();
				 String rubricResponseBody = allRubricFutures.get(i).get().getBody().toString();
				 String scene7StatusCode = allScene7Futures.get(i).get().getStatusCode().toString();
				 String scene7ResponseBody = allScene7Futures.get(i).get().getBody().toString();
				 if(!rubricResponseBody.equalsIgnoreCase(scene7ResponseBody)) {
					 fxgJsonOutputObjectList.add(new FxgJsonOutputObject(fxgurl,rubricStatusCode,null,scene7StatusCode,null,"Rubric and Scene7 Content mismatch"));
					   log.info("URL: {} | Rubric Status Code: {} | Scene7 Status code: {} | Reason : {}",fxgurl,rubricStatusCode,scene7StatusCode, "Rubric Content and Scene7 Content doesnt match");
					   log.info("--------------------------------------------------------------------------------------------------");

				 }
			}catch(ExecutionException serverException) {
				Throwable th = serverException.getCause();
				if(th instanceof HttpServerErrorException) {
					try {
							fxgJsonOutputObjectList.add(new FxgJsonOutputObject((urlList.get(i)),((HttpServerErrorException) th).getStatusCode().toString(),null,
									allScene7Futures.get(i).get().getStatusCode().toString(),null,"Internal Server error from Rubric"));
							log.info("URL: {} | Rubric Status Code: {} | Scene7 Status code: {}",fxgurl,((HttpServerErrorException) th).getStatusCode().toString(),allScene7Futures.get(i).get().getStatusCode());
							log.info("--------------------------------------------------------------------------------------------------");
					}catch(ExecutionException executionException) {
						Throwable thr = executionException.getCause();
						if(thr instanceof HttpServerErrorException) {
							fxgJsonOutputObjectList.add(new FxgJsonOutputObject((urlList.get(i)),((HttpServerErrorException) th).getStatusCode().toString(),null,
									((HttpServerErrorException) thr).getStatusCode().toString(),null,"Internal Server error from Scene7"));
							log.info("URL: {} | Rubric Status Code: {} | Scene7 Status code: {}",fxgurl, ((HttpServerErrorException) th).getStatusCode().toString(),((HttpServerErrorException) thr).getStatusCode().toString());
							log.info("--------------------------------------------------------------------------------------------------");	
						}
					}
				}
			}
			
		}
		return fxgJsonOutputObjectList;
		
		
	}
	
	
    public  void writeToCSV(List<FxgJsonOutputObject> fxgJsonOutputObjectList, String filePath)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            
            StringBuffer header = new StringBuffer();
            header.append("FXG-JSON URL");
            header.append(CSV_SEPARATOR);
            header.append("RubricStatusCode");
            header.append(CSV_SEPARATOR);
            header.append("S7StatusCode");
            header.append(CSV_SEPARATOR);
            header.append("ReasonForFailure");
            bw.write(header.toString());
            bw.newLine();
            for (FxgJsonOutputObject fxgJsonOutputObject : fxgJsonOutputObjectList)
            {
            	StringBuffer oneLine = new StringBuffer();
                oneLine.append(fxgJsonOutputObject.getUrl());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(fxgJsonOutputObject.getRubricStatusCode());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(fxgJsonOutputObject.getS7StatusCode());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(fxgJsonOutputObject.getComments());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) {}
        catch (FileNotFoundException e){}
        catch (IOException e){}
    }
}
