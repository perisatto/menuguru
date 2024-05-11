package com.perisatto.fiapprj.menuguru.customer.port.out;

import java.util.Optional;
import java.util.Set;

import com.perisatto.fiapprj.menuguru.customer.domain.model.CPF;
import com.perisatto.fiapprj.menuguru.customer.domain.model.Customer;

public interface ManageCustomerPort {
	Customer createCustomer(Customer customer) throws Exception;
	
	Optional<Customer> getCustomerByCPF(CPF customerDocument) throws Exception;
	
	Optional<Customer> getCustomerById(Long customerId) throws Exception;
	
	Optional<Customer> updateCustomer(Customer customer) throws Exception;
	
	Boolean deleteCustomer(Long customerId) throws Exception;

	Set<Customer> findAll(Integer limit, Integer offset) throws Exception;
}
