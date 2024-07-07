package com.perisatto.fiapprj.menuguru.hexagonal.order.adapter.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.perisatto.fiapprj.menuguru.hexagonal.customer.adapter.in.CustomerInternalAdpater;
import com.perisatto.fiapprj.menuguru.hexagonal.customer.domain.model.Customer;
import com.perisatto.fiapprj.menuguru.hexagonal.order.port.out.OrderCustomerPort;

@Component
public class OrderCustomerAdapter implements OrderCustomerPort {
	
	@Autowired
	private CustomerInternalAdpater customerInternalAdapter;
	
	@Override
	public Customer getCustomer(Long id) throws Exception {
		Customer customer = customerInternalAdapter.getCustomer(id);
		return customer;
	}

}
