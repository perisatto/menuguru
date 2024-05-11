package com.perisatto.fiapprj.menuguru.order.domain.model;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.menuguru.handler.exceptions.ValidationException;

public class Order {

	static final Logger logger = LogManager.getLogger(Order.class);
	private String messageValidation;

	private Long id;
	private OrderStatus status;
	private Long customerId;
	private Set<OrderItem> items;
	private Double totalPrice;


	public Order(OrderStatus orderStatus, Long customerId, Set<OrderItem> orderItems) throws Exception {
		if(!validate(orderStatus, customerId, orderItems)) {
			logger.warn(messageValidation);
			throw new ValidationException("ordr-1001", messageValidation);
		}

		Double totalPrice = 0.0;

		for(OrderItem item : orderItems) {
			totalPrice = totalPrice + (item.getActualPrice() * item.getQuantity());
		}

		this.status = orderStatus;
		this.customerId = customerId;
		this.items = orderItems;
		this.totalPrice = totalPrice;

	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Set<OrderItem> getItems() {
		return items;
	}
	public void setItems(Set<OrderItem> items) {
		this.items = items;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	private boolean validate(OrderStatus orderStatus, Long customerId, Set<OrderItem> orderItems) {
		messageValidation = "Error validating order data: ";		
		Boolean valid = true;

		if (customerId != null) {
			if(customerId == 0) {
				logger.debug("Error validating order data: invalid customer id");
				messageValidation = messageValidation + "invalid customer id |";
				valid = false;
			}
		}

		if(orderStatus == null) {
			logger.debug("Error validating order data: null order status");
			messageValidation = messageValidation + "null order status |";
			valid = false;
		}

		if((orderItems == null) || orderItems.isEmpty()) {
			logger.debug("Error validating order data: null or empty items list");
			messageValidation = messageValidation + "null or empty items list |";
			valid = false;			
		}

		return valid;
	}
}
