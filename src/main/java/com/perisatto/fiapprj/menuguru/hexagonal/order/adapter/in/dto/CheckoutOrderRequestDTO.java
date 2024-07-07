package com.perisatto.fiapprj.menuguru.hexagonal.order.adapter.in.dto;

public class CheckoutOrderRequestDTO {
	private String paymentIdentifier;

	public String getPaymentIdentifier() {
		return paymentIdentifier;
	}

	public void setPaymentIdentifier(String paymentIdentifier) {
		this.paymentIdentifier = paymentIdentifier;
	}
}
