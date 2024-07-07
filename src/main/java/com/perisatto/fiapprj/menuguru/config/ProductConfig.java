package com.perisatto.fiapprj.menuguru.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.perisatto.fiapprj.menuguru.application.interfaces.ProductRepository;
import com.perisatto.fiapprj.menuguru.application.usecases.ProductUseCase;
import com.perisatto.fiapprj.menuguru.infra.gateways.ProductRepositoryJpa;
import com.perisatto.fiapprj.menuguru.infra.gateways.mappers.ProductMapper;
import com.perisatto.fiapprj.menuguru.infra.persistences.repositories.ProductPersistenceRepository;

@Configuration
public class ProductConfig {

	@Bean
	ProductUseCase productUseCase(ProductRepository productRepository) {
		return new ProductUseCase(productRepository);
	}	
	
	@Bean
	ProductRepositoryJpa productJpaRepository(ProductPersistenceRepository productPersistenceRepository, ProductMapper productMapper) {
		return new ProductRepositoryJpa(productPersistenceRepository, productMapper);
	}
	
	@Bean
	ProductMapper productMapper() {
		return new ProductMapper();
	}
}
