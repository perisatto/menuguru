package com.perisatto.fiapprj.menuguru.infra.controllers;

import com.perisatto.fiapprj.menuguru.application.usecases.CustomerUseCase;
import com.perisatto.fiapprj.menuguru.domain.entities.customer.Customer;


public class CustomerApiController {
	
	private final CustomerUseCase customerUseCase;
	
	
	public CustomerApiController(CustomerUseCase customerUseCase) {
		this.customerUseCase = customerUseCase;
	}

	public Customer getCustomer(Long id) throws Exception {
		Customer customer = customerUseCase.getCustomerById(id);
		return customer;
	}
}
