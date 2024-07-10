package com.perisatto.fiapprj.menuguru.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.menuguru.application.interfaces.PaymentProcessor;
import com.perisatto.fiapprj.menuguru.application.usecases.PaymentUseCase;
import com.perisatto.fiapprj.menuguru.infra.gateways.PaymentWebApi;

@Configuration
public class PaymentConfig {

	@Autowired
	private Environment env;
	
	@Bean
	PaymentUseCase paymentUseCase(PaymentProcessor paymentProcessor) {
		return new PaymentUseCase(paymentProcessor);
	}
		
	@Bean
	PaymentWebApi paymentWebApi(){
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder(); 
		return new PaymentWebApi(restTemplateBuilder, this.env);
	}
}
