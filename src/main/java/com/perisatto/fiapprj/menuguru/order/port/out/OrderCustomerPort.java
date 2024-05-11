package com.perisatto.fiapprj.menuguru.order.port.out;

import com.perisatto.fiapprj.menuguru.customer.domain.model.Customer;

public interface OrderCustomerPort {
	Customer getCustomer(Long id) throws Exception;
}
