package com.perisatto.fiapprj.menuguru.application.usecases;

import com.perisatto.fiapprj.menuguru.application.interfaces.PaymentProcessor;
import com.perisatto.fiapprj.menuguru.domain.entities.order.Order;
import com.perisatto.fiapprj.menuguru.domain.entities.payment.Payment;

public class PaymentUseCase {
	
	private final PaymentProcessor paymentProcessor;
	
	public PaymentUseCase(PaymentProcessor paymentProcessor) {
		this.paymentProcessor = paymentProcessor;
	}

	public Payment createPayment(Order order) {
		Payment payment = new Payment(order);		
		paymentProcessor.createPayment(payment);
		return payment;
	}
}
