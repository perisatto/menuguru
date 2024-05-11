package com.perisatto.fiapprj.menuguru.customer.port.in;

import java.util.Set;

import com.perisatto.fiapprj.menuguru.customer.domain.model.Customer;

public interface ManageCustomerUseCase {
	
	Customer createCustomer(String documentNumber, String name, String email) throws Exception;

	Customer getCustomerByCPF(String documentNumber) throws Exception;
	
	Customer getCustomerById(Long customerId) throws Exception;
	
	Customer updateCustomer(Long customerId, String documentNumber, String customerName, String customerEmail) throws Exception;
	
	Boolean deleteCustomer(Long customerId) throws Exception;
	
	Set<Customer> findAllCustomers(Integer limit, Integer page) throws Exception;
}
