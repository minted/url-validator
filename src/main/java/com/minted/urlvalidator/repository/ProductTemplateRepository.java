package com.minted.urlvalidator.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.minted.urlvalidator.model.ProductTemplates;

public interface ProductTemplateRepository  extends CrudRepository<ProductTemplates, String>{
	
	@Query(value= "SELECT distinct mcc.skinnywrap_template, mcc.liner_template, mcc.rap_template, mcc.rcp_template, mcc.product_sku\n" + 
			"FROM minted_product_color_choice mcc\n" + 
			"JOIN product_template pt ON \n" + 
			"pt.product_sku = mcc.product_sku\n" + 
			"where pt.product_sku in (:productSku)\n" + 
			"AND skinnywrap_template is not null\n" + 
			"AND liner_template is not null\n" + 
			"AND rap_template is not null\n" + 
			"AND rcp_template is not null\n" + 
			"AND pt.updated_at > '2019-01-01'")
	List<ProductTemplates> findByProductSku(@Param("productSku") List<String> productSku);
}
