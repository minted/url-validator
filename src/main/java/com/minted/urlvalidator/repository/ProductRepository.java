package com.minted.urlvalidator.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.minted.urlvalidator.model.ProductFXG;
import com.minted.urlvalidator.model.ProductTemplates;

public interface ProductRepository extends CrudRepository<ProductFXG, String> {
	
	@Query(value = "SELECT DISTINCT sdt.fxg_filename, sd.product_sku\n" + 
			"FROM minted_saved_design sd\n" + 
			"JOIN saved_design_template sdt ON sd.id = sdt.saved_design_id\n" + 
			"WHERE sd.product_sku IN (:productSku)\n" + 
			"AND sd.modification_date >= '2019-01-01'\n" + 
			"AND fxg_filename IS NOT NULL\n"
			+ "ORDER BY fxg_filename ASC")
	
	List<ProductFXG> findByProductSku(@Param("productSku")List<String> productSku);

}
