
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
	
	
			
	private List<CompletableFuture<ResponseEntity<String>>> allRubricFutures = new ArrayList<>();
	
	private List<CompletableFuture<ResponseEntity<String>>> allScene7Futures = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public void fxgJsonResponseComparator(List<String> urlList) throws InterruptedException, ExecutionException{

	    List<FxgJsonOutputObject> fxgJsonOutputObjectList = new ArrayList<FxgJsonOutputObject>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie" , "renderEndpoint=rubric");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		
		urlList.forEach(url -> {
			allScene7Futures.add(endpointCall.callUrl(url, null));
	    	allRubricFutures.add(endpointCall.callUrl(url, entity));
	    	});
		
		
		
		for (int i = 0; i < urlList.size(); i++) {
			try {
				fxgJsonOutputObjectList.add(new FxgJsonOutputObject((urlList.get(i)),allRubricFutures.get(i).get().getStatusCode().toString(),allRubricFutures.get(i).get().getBody().toString(),
						allScene7Futures.get(i).get().getStatusCode().toString(),allScene7Futures.get(i).get().getBody().toString()));
				log.info("URL: {} ",urlList.get(i));
				log.info("Rubric Status Code: {} ",allRubricFutures.get(i).get().getStatusCode().toString());
				log.info("Rubric Status Message: {} ",allRubricFutures.get(i).get().getBody().toString());
				log.info("Scene7 Status code: {} ",allScene7Futures.get(i).get().getStatusCode());
				log.info("Scene7 Status Message: {} ",allScene7Futures.get(i).get().getBody().toString());
				log.info("--------------------------------------------------------------------------------------------------");
			}catch(ExecutionException serverException) {
				Throwable th = serverException.getCause();
				if(th instanceof HttpServerErrorException) {
					fxgJsonOutputObjectList.add(new FxgJsonOutputObject((urlList.get(i)),((HttpServerErrorException) th).getStatusCode().toString(),((HttpServerErrorException) th).getStatusText(),
							allScene7Futures.get(i).get().getStatusCode().toString(),allScene7Futures.get(i).get().getBody().toString() ));
					log.info("URL: {} ",urlList.get(i));
					log.info("Rubric Status Code: {} ",((HttpServerErrorException) th).getStatusCode().toString());
					log.info("Rubric Status Message: {} ",((HttpServerErrorException) th).getStatusText());
					log.info("Scene7 Status code: {} ",allScene7Futures.get(i).get().getStatusCode());
					log.info("Scene7 Status Message: {} ",allScene7Futures.get(i).get().getBody().toString());
					log.info("--------------------------------------------------------------------------------------------------");
				}
			}
			
		}
		log.info("Writing the results to file");
		writeToCSV(fxgJsonOutputObjectList, "src/main/resources/report/resultfile");
		
	}
	
	
    private static void writeToCSV(List<FxgJsonOutputObject> fxgJsonOutputObjectList, String filePath)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            
            StringBuffer header = new StringBuffer();
            header.append("FXG-JSON URL");
            header.append(CSV_SEPARATOR);
            header.append("RubricStatusCode");
            header.append(CSV_SEPARATOR);
            header.append("RubricStatusBody");
            header.append(CSV_SEPARATOR);
            header.append("S7StatusCode");
            header.append(CSV_SEPARATOR);
            header.append("S7StatusBody");
            bw.write(header.toString());
            bw.newLine();
            for (FxgJsonOutputObject fxgJsonOutputObject : fxgJsonOutputObjectList)
            {
            	StringBuffer oneLine = new StringBuffer();
                oneLine.append(fxgJsonOutputObject.getUrl());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(fxgJsonOutputObject.getRubricStatusCode());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(fxgJsonOutputObject.getRubricStatusBody());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(fxgJsonOutputObject.getS7StatusCode());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(fxgJsonOutputObject.getS7StatusBody());
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
