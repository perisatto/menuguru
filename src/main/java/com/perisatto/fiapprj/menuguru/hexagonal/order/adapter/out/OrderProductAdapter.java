package com.perisatto.fiapprj.menuguru.hexagonal.order.adapter.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.perisatto.fiapprj.menuguru.hexagonal.order.port.out.OrderProductPort;
import com.perisatto.fiapprj.menuguru.hexagonal.product.adapter.in.ProductInternalAdapter;
import com.perisatto.fiapprj.menuguru.hexagonal.product.domain.model.Product;

@Component
public class OrderProductAdapter implements OrderProductPort {

	@Autowired
	private ProductInternalAdapter productInternalAdapter;
	
	
	@Override
	public Product getProduct(Long id) throws Exception {
		Product product = productInternalAdapter.getProduct(id);
		return product;
	}

}
