package com.perisatto.fiapprj.menuguru.application.interfaces;

import com.perisatto.fiapprj.menuguru.domain.entities.payment.Payment;

public interface PaymentProcessor {
	Payment createPayment(Payment payment) throws Exception;
}
