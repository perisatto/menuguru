package com.perisatto.fiapprj.menuguru.infra.gateways;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.perisatto.fiapprj.menuguru.application.interfaces.CustomerRepository;
import com.perisatto.fiapprj.menuguru.domain.entities.customer.CPF;
import com.perisatto.fiapprj.menuguru.domain.entities.customer.Customer;
import com.perisatto.fiapprj.menuguru.infra.gateways.mappers.CustomerMapper;
import com.perisatto.fiapprj.menuguru.infra.persistences.entities.CustomerEntity;
import com.perisatto.fiapprj.menuguru.infra.persistences.repositories.CustomerPersistenceRepository;

public class CustomerRepositoyJpa implements CustomerRepository {
	
	private final CustomerPersistenceRepository customerRepository;
	private final CustomerMapper customerMapper;

	public CustomerRepositoyJpa(CustomerPersistenceRepository customerRepository, CustomerMapper customerMapper) {
		this.customerRepository = customerRepository;
		this.customerMapper = customerMapper;
	}

	public Optional<Customer> getCustomerByCPF(CPF customerDocument) throws Exception {
		Customer customer;

		Optional<CustomerEntity> customerJpaEntity = customerRepository.findByDocumentNumberAndIdCustomerStatus(customerDocument.getDocumentNumber(), 1L);
		if(customerJpaEntity.isPresent()) {				
			customer = customerMapper.mapToDomainEntity(customerJpaEntity.get());
		}else {
			return Optional.empty();
		}

		return Optional.of(customer);
	}
	
	public Optional<Customer> getCustomerById(Long customerId) throws Exception{
		Customer customer;

		Optional<CustomerEntity> customerJpaEntity = customerRepository.findByIdCustomerAndIdCustomerStatus(customerId, 1L);
		if(customerJpaEntity.isPresent()) {				
			customer = customerMapper.mapToDomainEntity(customerJpaEntity.get());
		}else {
			return Optional.empty();
		}

		return Optional.of(customer);
	}

	@Override
	public Customer createCustomer(Customer customer) throws Exception {
		CustomerEntity customerJpaEntity =  customerMapper.mapToJpaEntity(customer);
		customerJpaEntity.setIdCustomerStatus(1L);
		customerJpaEntity = customerRepository.save(customerJpaEntity);
		Customer newCustomer;
		newCustomer = customerMapper.mapToDomainEntity(customerJpaEntity);
		return newCustomer;
	}

	@Override
	public Optional<Customer> updateCustomer(Customer customer) throws Exception {
		CustomerEntity customerJpaEntity = customerMapper.mapToJpaEntity(customer);
		customerJpaEntity.setIdCustomerStatus(1L);
		customerJpaEntity = customerRepository.save(customerJpaEntity);
		Customer updatedCustomer = customerMapper.mapToDomainEntity(customerJpaEntity);
		return Optional.of(updatedCustomer);
	}

	@Override
	public Boolean deleteCustomer(Long customerId) throws Exception {
		Optional<CustomerEntity> customer = customerRepository.findById(customerId);
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
		Page<CustomerEntity> customers = customerRepository.findByIdCustomerStatus(1L, pageable);
		Set<Customer> customersSet = new LinkedHashSet<Customer>();
		
		for (CustomerEntity customer : customers) {
			Customer retrievedCustomer = new Customer(customer.getIdCustomer(), new CPF(customer.getDocumentNumber()), customer.getName(), customer.geteMail());
			customersSet.add(retrievedCustomer);
		}
		return customersSet;
	}

}
