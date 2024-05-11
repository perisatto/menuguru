package com.perisatto.fiapprj.menuguru.order.adapter.out.repository;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"Order\"")
public class OrderJpaEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOrder;
	private Long idOrderStatus;
	private Long idCustomer;
	private Double totalPrice;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "idOrder", nullable=false)
	private Set<OrderItemJpaEntity> items;
	
	public Long getIdOrder() {
		return idOrder;
	}
	
	public void setIdOrder(Long idOrder) {
		this.idOrder = idOrder;
	}
	
	public Long getIdOrderStatus() {
		return idOrderStatus;
	}
	
	public void setIdOrderStatus(Long idOrderStatus) {
		this.idOrderStatus = idOrderStatus;
	}
	
	public Long getIdCustomer() {
		return idCustomer;
	}
	
	public void setIdCustomer(Long idCustomer) {
		this.idCustomer = idCustomer;
	}
	
	public Double getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public Set<OrderItemJpaEntity> getItems() {
		return items;
	}
	
	public void setItems(Set<OrderItemJpaEntity> items) {
		this.items = items;
	}
}
