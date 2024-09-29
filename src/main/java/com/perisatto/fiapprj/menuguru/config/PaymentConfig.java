package com.perisatto.fiapprj.menuguru.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.menuguru.application.interfaces.PaymentProcessor;
import com.perisatto.fiapprj.menuguru.application.interfaces.PaymentRepository;
import com.perisatto.fiapprj.menuguru.application.usecases.PaymentUseCase;
import com.perisatto.fiapprj.menuguru.infra.gateways.PaymentMongoRepository;
import com.perisatto.fiapprj.menuguru.infra.gateways.PaymentWebApi;
import com.perisatto.fiapprj.menuguru.infra.persistences.repositories.PaymentPersistenceRepository;

@Configuration
public class PaymentConfig {

	@Autowired
	private Environment env;

	@Bean
	PaymentUseCase paymentUseCase(PaymentProcessor paymentProcessor, PaymentRepository paymentRepository) {
		return new PaymentUseCase(paymentProcessor, paymentRepository);
	}

	@Bean
	PaymentWebApi paymentWebApi(){
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder(); 
		return new PaymentWebApi(restTemplateBuilder, this.env);
	}

	@Bean
	PaymentMongoRepository paymentMongoRepository(PaymentPersistenceRepository paymentPersistenceRepository) {
		PaymentMongoRepository paymentMongoRepository = new PaymentMongoRepository(paymentPersistenceRepository);
		return paymentMongoRepository;
	}
	
}
