package com.perisatto.fiapprj.menuguru.hexagonal.order.port.out;

import com.perisatto.fiapprj.menuguru.hexagonal.customer.domain.model.Customer;

public interface OrderCustomerPort {
	Customer getCustomer(Long id) throws Exception;
}
