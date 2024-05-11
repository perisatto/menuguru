package com.perisatto.fiapprj.menuguru.order.port.out;

import com.perisatto.fiapprj.menuguru.product.domain.model.Product;

public interface OrderProductPort {
	Product getProduct(Long id) throws Exception;
}
