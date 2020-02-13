package com.minted.urlvalidator.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.minted.urlvalidator.model.ProductFXG;
import com.minted.urlvalidator.model.ProductTemplates;

@Repository
public class ProductRepositoryImplementation {
	
	@Autowired
	private ProductRepository urlRepository;
	
	@Autowired
	private ProductTemplateRepository productTemplateRepository;
	
	public List<ProductFXG> getTemplates(List<String> productSku){
		return urlRepository.findByProductSku(productSku);
	}
	
	public List<ProductTemplates> getAllTemplates(List<String> productSku){
		return productTemplateRepository.findByProductSku(productSku);
	}

}
