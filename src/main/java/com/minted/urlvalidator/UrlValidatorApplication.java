package com.minted.urlvalidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.minted.urlvalidator.service.FxgJSONURLGeneratorService;
import com.minted.urlvalidator.service.URLValidatorService;

@SpringBootApplication
public class UrlValidatorApplication implements CommandLineRunner{
	
	
	private static final String SKU_LIST_PATH = "src/main/resources/skulist.txt";
	
	@Autowired
	private FxgJSONURLGeneratorService fxgjsonURLGeneratorService;
	
	@Autowired
	private URLValidatorService urlValidatorService;

	public static void main(String[] args) {
		SpringApplication.run(UrlValidatorApplication.class, args);
	}
	
	@Override
	public void run(String args[]) throws IOException, InterruptedException, ExecutionException {
		
	   List<String> fxgJsonURLList = fxgjsonURLGeneratorService.generateFXGJsonURLFromFile(SKU_LIST_PATH);
	   
	   System.out.println(LocalDateTime.now());
	   Map<String, String> urlResult = urlValidatorService.fxgJsonUrlValidator(fxgJsonURLList);
	   
	   writeResultsToCSV(urlResult);
	   System.out.println(LocalDateTime.now());
	   
	}

	private void writeResultsToCSV(Map<String, String> urlResult) throws IOException {
		Path out = Paths.get("src/main/resources/output.csv");
		Files.write(out, () -> urlResult.entrySet().stream()
			    .<CharSequence>map(e -> e.getKey() + "," + e.getValue())
			    .iterator());
	}
}
