package com.perisatto.fiapprj.menuguru.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.perisatto.fiapprj.menuguru.application.interfaces.CustomerRepository;
import com.perisatto.fiapprj.menuguru.application.interfaces.OrderRepository;
import com.perisatto.fiapprj.menuguru.application.interfaces.ProductRepository;
import com.perisatto.fiapprj.menuguru.application.usecases.OrderUseCase;
import com.perisatto.fiapprj.menuguru.infra.gateways.OrderRepositoryJpa;
import com.perisatto.fiapprj.menuguru.infra.gateways.mappers.OrderMapper;
import com.perisatto.fiapprj.menuguru.infra.persistences.repositories.OrderPersistenceRepository;

@Configuration
public class OrderConfig {
	
	@Bean
	OrderUseCase orderUseCase(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
		return new OrderUseCase(orderRepository, customerRepository, productRepository);
	}
	
	@Bean
	OrderRepositoryJpa orderJpaRepository(OrderPersistenceRepository orderPersistenceRepository, OrderMapper orderMapper) {
		return new OrderRepositoryJpa(orderPersistenceRepository, orderMapper);
	}
	
	@Bean
	OrderMapper orderMapper() {
		return new OrderMapper();
	}
}
