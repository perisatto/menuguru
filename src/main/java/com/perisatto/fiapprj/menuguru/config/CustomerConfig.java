package com.perisatto.fiapprj.menuguru.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.perisatto.fiapprj.menuguru.application.interfaces.CustomerRepository;
import com.perisatto.fiapprj.menuguru.application.usecases.CustomerUseCase;
import com.perisatto.fiapprj.menuguru.infra.gateways.CustomerRepositoyJpa;
import com.perisatto.fiapprj.menuguru.infra.gateways.mappers.CustomerMapper;
import com.perisatto.fiapprj.menuguru.infra.persistences.repositories.CustomerPersistenceRepository;

@Configuration
public class CustomerConfig {

	@Bean
	CustomerUseCase customerUseCase(CustomerRepository customerRepository) {
		return new CustomerUseCase(customerRepository);
	}	
	
	@Bean
	CustomerRepositoyJpa customerJpaRepository(CustomerPersistenceRepository customerPersistenceRepository, CustomerMapper customerMapper) {
		return new CustomerRepositoyJpa(customerPersistenceRepository, customerMapper);
	}
	
	@Bean
	CustomerMapper customerMapper() {
		return new CustomerMapper();
	}
}
