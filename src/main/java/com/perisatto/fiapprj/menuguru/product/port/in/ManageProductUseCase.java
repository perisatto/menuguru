package com.perisatto.fiapprj.menuguru.product.port.in;

import java.util.Set;

import com.perisatto.fiapprj.menuguru.product.domain.model.Product;

public interface ManageProductUseCase {
	Product createProduct(String name, String type, String description, Double price, String image) throws Exception;
	
	Product getProduct(Long id) throws Exception;
	
	Product updateProduct(Long id, String name, String type, String description, Double price, String image) throws Exception;
	
	Boolean deleteProduct(Long id) throws Exception;
	
	Set<Product> findAllProducts(Integer limit, Integer page, String type) throws Exception;
}
