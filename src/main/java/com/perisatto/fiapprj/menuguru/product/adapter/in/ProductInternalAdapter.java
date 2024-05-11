package com.perisatto.fiapprj.menuguru.product.adapter.in;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.perisatto.fiapprj.menuguru.product.domain.model.Product;
import com.perisatto.fiapprj.menuguru.product.port.in.ManageProductUseCase;

@Controller
public class ProductInternalAdapter {
	
	@Autowired
	private ManageProductUseCase manageProductUseCase;
	
	public Product getProduct(Long productId) throws Exception {
		Product product = manageProductUseCase.getProduct(productId);
		return product;
	}
}
