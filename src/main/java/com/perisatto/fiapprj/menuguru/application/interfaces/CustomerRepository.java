package com.perisatto.fiapprj.menuguru.application.interfaces;

import java.util.Optional;
import java.util.Set;

import com.perisatto.fiapprj.menuguru.domain.entities.customer.CPF;
import com.perisatto.fiapprj.menuguru.domain.entities.customer.Customer;

public interface CustomerRepository {

	Customer createCustomer(Customer customer) throws Exception;
	
	Optional<Customer> getCustomerByCPF(CPF customerDocument) throws Exception;
	
	Optional<Customer> getCustomerById(Long customerId) throws Exception;
	
	Optional<Customer> updateCustomer(Customer customer) throws Exception;
	
	Boolean deleteCustomer(Long customerId) throws Exception;

	Set<Customer> findAll(Integer limit, Integer offset) throws Exception;
}
