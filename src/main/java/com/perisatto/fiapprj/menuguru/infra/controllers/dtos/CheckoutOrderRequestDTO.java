package com.perisatto.fiapprj.menuguru.infra.controllers.dtos;

public class CheckoutOrderRequestDTO {
	private String paymentIdentifier;

	public String getPaymentIdentifier() {
		return paymentIdentifier;
	}

	public void setPaymentIdentifier(String paymentIdentifier) {
		this.paymentIdentifier = paymentIdentifier;
	}
}
