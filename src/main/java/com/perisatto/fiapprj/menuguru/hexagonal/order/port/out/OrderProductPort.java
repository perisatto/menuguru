package com.perisatto.fiapprj.menuguru.hexagonal.order.port.out;

import com.perisatto.fiapprj.menuguru.hexagonal.product.domain.model.Product;

public interface OrderProductPort {
	Product getProduct(Long id) throws Exception;
}
