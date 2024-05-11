package com.perisatto.fiapprj.menuguru.customer.adapter.out;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.perisatto.fiapprj.menuguru.customer.adapter.out.repository.CustomerJpaEntity;
import com.perisatto.fiapprj.menuguru.customer.adapter.out.repository.CustomerRepository;
import com.perisatto.fiapprj.menuguru.customer.domain.model.CPF;
import com.perisatto.fiapprj.menuguru.customer.domain.model.Customer;
import com.perisatto.fiapprj.menuguru.customer.port.out.ManageCustomerPort;

@Component
public class CustomerPersistenceApdapter implements ManageCustomerPort {

	private CustomerRepository customerRepository;

	public CustomerPersistenceApdapter(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	public Optional<Customer> getCustomerByCPF(CPF customerDocument) throws Exception {
		Customer customer;

		Optional<CustomerJpaEntity> customerJpaEntity = customerRepository.findByDocumentNumberAndIdCustomerStatus(customerDocument.getDocumentNumber(), 1L);
		if(customerJpaEntity.isPresent()) {
			CustomerMapper customerMapper = new CustomerMapper();				
			customer = customerMapper.mapToDomainEntity(customerJpaEntity.get());
		}else {
			return Optional.empty();
		}

		return Optional.of(customer);
	}
	
	public Optional<Customer> getCustomerById(Long customerId) throws Exception{
		Customer customer;

		Optional<CustomerJpaEntity> customerJpaEntity = customerRepository.findByIdCustomerAndIdCustomerStatus(customerId, 1L);
		if(customerJpaEntity.isPresent()) {
			CustomerMapper customerMapper = new CustomerMapper();				
			customer = customerMapper.mapToDomainEntity(customerJpaEntity.get());
		}else {
			return Optional.empty();
		}

		return Optional.of(customer);
	}

	@Override
	public Customer createCustomer(Customer customer) throws Exception {
		CustomerMapper customerMapper = new CustomerMapper();
		CustomerJpaEntity customerJpaEntity =  customerMapper.mapToJpaEntity(customer);
		customerJpaEntity.setIdCustomerStatus(1L);
		customerJpaEntity = customerRepository.save(customerJpaEntity);
		Customer newCustomer;
		newCustomer = customerMapper.mapToDomainEntity(customerJpaEntity);
		return newCustomer;
	}

	@Override
	public Optional<Customer> updateCustomer(Customer customer) throws Exception {
		CustomerMapper customerMapper = new CustomerMapper();
		CustomerJpaEntity customerJpaEntity = customerMapper.mapToJpaEntity(customer);
		customerJpaEntity.setIdCustomerStatus(1L);
		customerJpaEntity = customerRepository.save(customerJpaEntity);
		Customer updatedCustomer = customerMapper.mapToDomainEntity(customerJpaEntity);
		return Optional.of(updatedCustomer);
	}

	@Override
	public Boolean deleteCustomer(Long customerId) throws Exception {
		Optional<CustomerJpaEntity> customer = customerRepository.findById(customerId);
		if(customer.isPresent()) {
			customer.get().setIdCustomerStatus(2L);
			customerRepository.save(customer.get());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Set<Customer> findAll(Integer limit, Integer page) throws Exception {
		Pageable pageable = PageRequest.of(page, limit, Sort.by("idCustomer"));
		Page<CustomerJpaEntity> customers = customerRepository.findByIdCustomerStatus(1L, pageable);
		Set<Customer> customersSet = new LinkedHashSet<Customer>();
		
		for (CustomerJpaEntity customer : customers) {
			Customer retrievedCustomer = new Customer(customer.getIdCustomer(), new CPF(customer.getDocumentNumber()), customer.getName(), customer.geteMail());
			customersSet.add(retrievedCustomer);
		}
		return customersSet;
	}
}
