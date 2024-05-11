package com.perisatto.fiapprj.menuguru.order.adapter.out;

import com.perisatto.fiapprj.menuguru.customer.adapter.in.CustomerInternalAdpater;
import com.perisatto.fiapprj.menuguru.customer.domain.model.Customer;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderCustomerPort;

public class OrderCustomerAdapter implements OrderCustomerPort {

	@Override
	public Customer getCustomer(Long id) throws Exception {
		CustomerInternalAdpater customerAdapter = new CustomerInternalAdpater();
		Customer customer = customerAdapter.getCustomer(id);
		return customer;
	}

}
