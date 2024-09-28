package com.perisatto.fiapprj.menuguru.application.usecases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.menuguru.application.interfaces.PaymentProcessor;
import com.perisatto.fiapprj.menuguru.application.interfaces.PaymentRepository;
import com.perisatto.fiapprj.menuguru.domain.entities.order.Order;
import com.perisatto.fiapprj.menuguru.domain.entities.payment.Payment;

public class PaymentUseCase {
	
	static final Logger logger = LogManager.getLogger(PaymentUseCase.class);
	private final PaymentProcessor paymentProcessor;	
	
	public PaymentUseCase(PaymentProcessor paymentProcessor, PaymentRepository paymentRepository) {
		this.paymentProcessor = paymentProcessor;
	}

	public Payment createPayment(Order order) throws Exception {
		Payment payment = new Payment(order);		
		paymentProcessor.createPayment(payment);
		return payment;
	}

	public Boolean registerPayment(String paymentData) {
		logger.info("Registering payment...");
		
		
		
		return true;
	}
	
	
}
