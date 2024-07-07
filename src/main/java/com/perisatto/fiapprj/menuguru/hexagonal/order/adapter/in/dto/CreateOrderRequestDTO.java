package com.perisatto.fiapprj.menuguru.hexagonal.order.adapter.in.dto;

import java.util.LinkedHashSet;
import java.util.Set;

public class CreateOrderRequestDTO {
	private Long customerId;
	private Set<OrderItemDTO> items = new LinkedHashSet<OrderItemDTO>();
	
	public Long getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public Set<OrderItemDTO> getItems() {
		return items;
	}
	
	public void setItems(Set<OrderItemDTO> products) {
		this.items = products;
	}
}
