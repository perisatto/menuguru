package com.perisatto.fiapprj.menuguru.customer.adapter.in;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.perisatto.fiapprj.menuguru.customer.domain.model.Customer;
import com.perisatto.fiapprj.menuguru.customer.port.in.ManageCustomerUseCase;

@Controller
public class CustomerInternalAdpater {
	
	@Autowired
	private ManageCustomerUseCase manageCustomerUseCase;
	
	public Customer getCustomer(Long id) throws Exception {
		Customer customer = manageCustomerUseCase.getCustomerById(id);
		return customer;
	}
}
