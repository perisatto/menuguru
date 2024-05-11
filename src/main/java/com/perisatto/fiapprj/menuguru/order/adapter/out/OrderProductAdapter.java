package com.perisatto.fiapprj.menuguru.order.adapter.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.perisatto.fiapprj.menuguru.order.port.out.OrderProductPort;
import com.perisatto.fiapprj.menuguru.product.adapter.in.ProductInternalAdapter;
import com.perisatto.fiapprj.menuguru.product.domain.model.Product;

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
