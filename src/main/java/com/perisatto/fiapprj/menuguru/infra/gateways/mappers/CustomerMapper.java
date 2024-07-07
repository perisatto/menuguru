package com.perisatto.fiapprj.menuguru.infra.gateways.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import com.perisatto.fiapprj.menuguru.domain.entities.customer.CPF;
import com.perisatto.fiapprj.menuguru.domain.entities.customer.Customer;
import com.perisatto.fiapprj.menuguru.infra.persistences.entities.CustomerEntity;

public class CustomerMapper {
	
	private ModelMapper modelMapper;
	
	public Customer mapToDomainEntity(CustomerEntity customer) throws Exception {
		CPF documentNumber = new CPF(customer.getDocumentNumber());
		Customer customerDomainEntity = new Customer(customer.getIdCustomer(), documentNumber, customer.getName(), customer.geteMail());
		return customerDomainEntity;
	}
	
	public CustomerEntity mapToJpaEntity(Customer customer) {
		this.modelMapper = new ModelMapper();
		
		TypeMap<Customer, CustomerEntity> propertyMapper = this.modelMapper.createTypeMap(Customer.class, CustomerEntity.class);
		propertyMapper.addMapping(Customer::getId, CustomerEntity::setIdCustomer);
		propertyMapper.addMapping(Customer::getName, CustomerEntity::setName);
		propertyMapper.addMapping(Customer::getEmail, CustomerEntity::seteMail);
		
		CustomerEntity customerJpaEntity = modelMapper.map(customer, CustomerEntity.class);
		customerJpaEntity.setDocumentNumber(customer.getDocumentNumber().getDocumentNumber());
		return customerJpaEntity;
		
	}
}
