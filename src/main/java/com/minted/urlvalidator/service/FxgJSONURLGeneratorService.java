package com.minted.urlvalidator.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minted.urlvalidator.model.ProductFXG;
import com.minted.urlvalidator.model.ProductTemplates;
import com.minted.urlvalidator.repository.ProductRepositoryImplementation;

@Service
public class FxgJSONURLGeneratorService {
	
private static final String HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON = "https://www.minted.com/personalize/fxg/json/";
	
	@Autowired
	ProductRepositoryImplementation productRepositoryImplementation;
	
	public List<String> generateFXGJsonURLFromFile(String filePath) throws IOException{
		 
		 List<String> productSKU = Files.readAllLines(Paths.get(filePath));
		 List<ProductFXG> fxgList =  productRepositoryImplementation.getTemplates(productSKU);
		   
		   List<ProductTemplates> templatesList = productRepositoryImplementation.getAllTemplates(productSKU);
		   
		   List<String> fxgJSONURLList = new ArrayList<String>();
		   
		   if(!fxgList.isEmpty() || fxgList.size() != 0) {
			   for (ProductFXG url : fxgList) {
				   if(!url.getFxg_filename().isEmpty()) {
					   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + url.getFxg_filename() + "?sku=" + url.getProductSku());
			   
				   }
			   }
		   }
		   
		   if(templatesList.size() != 0 || !templatesList.isEmpty()) {
			   for (ProductTemplates template : templatesList) {
				   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + template.getLinerTemplate() + "?sku=" + template.getProductSku());
				   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + template.getRapTemplate() + "?sku=" + template.getProductSku());
				   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + template.getRcpTemplate() + "?sku=" + template.getProductSku());
				   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + template.getSkinnywrapTemplate() + "?sku=" + template.getProductSku());
			   }
		   } 
		   
		   return fxgJSONURLList;
	}

}
