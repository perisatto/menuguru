package com.perisatto.fiapprj.menuguru.customer.adapter.out;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import com.perisatto.fiapprj.menuguru.customer.adapter.out.repository.CustomerJpaEntity;
import com.perisatto.fiapprj.menuguru.customer.domain.model.CPF;
import com.perisatto.fiapprj.menuguru.customer.domain.model.Customer;

public class CustomerMapper {
	
	private ModelMapper modelMapper;
	
	Customer mapToDomainEntity(CustomerJpaEntity customer) throws Exception {
		CPF documentNumber = new CPF(customer.getDocumentNumber());
		Customer customerDomainEntity = new Customer(customer.getIdCustomer(), documentNumber, customer.getName(), customer.geteMail());
		return customerDomainEntity;
	}
	
	CustomerJpaEntity mapToJpaEntity(Customer customer) {
		this.modelMapper = new ModelMapper();
		
		TypeMap<Customer, CustomerJpaEntity> propertyMapper = this.modelMapper.createTypeMap(Customer.class, CustomerJpaEntity.class);
		propertyMapper.addMapping(Customer::getId, CustomerJpaEntity::setIdCustomer);
		propertyMapper.addMapping(Customer::getName, CustomerJpaEntity::setName);
		propertyMapper.addMapping(Customer::getEmail, CustomerJpaEntity::seteMail);
		
		CustomerJpaEntity customerJpaEntity = modelMapper.map(customer, CustomerJpaEntity.class);
		customerJpaEntity.setDocumentNumber(customer.getDocumentNumber().getDocumentNumber());
		return customerJpaEntity;
		
	}
}
