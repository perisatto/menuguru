package com.perisatto.fiapprj.menuguru.infra.controllers;

import com.perisatto.fiapprj.menuguru.application.usecases.ProductUseCase;
import com.perisatto.fiapprj.menuguru.domain.entities.product.Product;


public class ProductApiController {
	
	private final ProductUseCase productUseCase;

	public ProductApiController(ProductUseCase productUseCase) {
		this.productUseCase = productUseCase;
	}
	
	public Product getProduct(Long productId) throws Exception {
		Product product = productUseCase.getProduct(productId);
		return product;
	}
}
