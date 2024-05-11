package com.perisatto.fiapprj.menuguru.product.port.out;

import java.util.Optional;
import java.util.Set;

import com.perisatto.fiapprj.menuguru.product.domain.model.Product;

public interface ManageProductPort {
	Product createProduct(Product product) throws Exception;
	
	Optional<Product> getProductById(Long id) throws Exception;
	
	Optional<Product> updateProduct(Product product) throws Exception;
	
	Boolean deleteProduct(Long id) throws Exception;

	Set<Product> findAll(Integer limit, Integer offset, String type) throws Exception;
}
