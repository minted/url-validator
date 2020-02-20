package com.minted.urlvalidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import com.minted.urlvalidator.model.FxgJsonOutputObject;
import com.minted.urlvalidator.service.FxgJSONURLGeneratorService;
import com.minted.urlvalidator.service.URLValidatorService;

@SpringBootApplication
@EnableAsync
public class UrlValidatorApplication implements CommandLineRunner {
	
	private static final String SKU_LIST_PATH = "test-sku-data";
	
	private Logger logger = LoggerFactory.getLogger(UrlValidatorApplication.class);
	
	@Autowired
	private FxgJSONURLGeneratorService fxgjsonURLGeneratorService;
	
	@Autowired
	private URLValidatorService urlValidatorService;
	
	@Autowired
	private static ConfigurableApplicationContext context;
	
	public static void main(String[] args) {
		context = SpringApplication.run(UrlValidatorApplication.class, args);
		context.close();
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		logger.info("--------------------------------------------------------------------------------------------------------------------------------");
		logger.info("Starting the validation of fxg json url's");
		
	   List<String> fxgJsonURLList = fxgjsonURLGeneratorService.generateFXGJsonURLFromFile(SKU_LIST_PATH);
	   
	   logger.info("--------------------------------------------------------------------------------------------------------------------------------");
	   logger.info("Total count of the fxg json url's fetched from db : {}",fxgJsonURLList.size());
		
	   logger.info("--------------------------------------------------------------------------------------------------------------------------------"); 
	   logger.info("Starting to get the status of the fxg json urls at: {}",LocalDateTime.now()); 
	   
	   logger.info("--------------------------------------------------------------------------------------------------------------------------------"); 
	   logger.info("Keep your fingers crossed. The service returns the status of the URL's for Rubric and Scene7 along with the response body"); 
	   
	   List<FxgJsonOutputObject> fxgJsonOutputObjectList =  urlValidatorService.fxgJsonResponseComparator(fxgJsonURLList);
	   logger.info("Writing the results to file");
	   
	   urlValidatorService.writeToCSV(fxgJsonOutputObjectList, "src/main/resources/report/resultfile");
	   
	   logger.info("--------------------------------------------------------------------------------------------------------------------------------");
	   logger.info("Finished retrieving the status of the fxg json urls at:{} : and list of URL's : {} ",LocalDateTime.now() , fxgJsonURLList.size());
	   logger.info("The resulting output.csv file will emailed to the recipients in the jenkins job. if the attachment is not received then please check the folder src/main/resources under the workspace in the slave for the output csv file" ); 
	   logger.info("--------------------------------------------------------------------------------------------------------------------------------");
	   
		
	}
}
