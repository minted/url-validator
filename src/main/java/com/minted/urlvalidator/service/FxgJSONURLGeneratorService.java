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
		 List<ProductFXG> urlList =  productRepositoryImplementation.getTemplates(productSKU);
		   
		   List<ProductTemplates> templatesList = productRepositoryImplementation.getAllTemplates(productSKU);
		   
		   List<String> fxgJSONURLList = new ArrayList<String>();
		   for (ProductFXG url : urlList) {
			   if(!url.getFxg_filename().isEmpty()) {
				   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + url.getFxg_filename() + "?sku=" + url.getProductSku());
		   
			   }
		   }
		   
		   for (ProductTemplates string : templatesList) {
			   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + string.getLinerTemplate() + "?sku=" + string.getProductSku());
			   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + string.getRapTemplate() + "?sku=" + string.getProductSku());
			   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + string.getRcpTemplate() + "?sku=" + string.getProductSku());
			   fxgJSONURLList.add(HTTPS_WWW_MINTED_COM_PERSONALIZE_FXG_JSON + string.getSkinnywrapTemplate() + "?sku=" + string.getProductSku());
		   }
		   return fxgJSONURLList;
	}

}
