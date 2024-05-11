package com.perisatto.fiapprj.menuguru.order.domain.model;

public enum OrderStatus {
	RECEBIDO(1),
	EM_PREPARACAO(2),
	PRONTO(3),
	FINALIZADO(4);
	
	private Integer id;
	
	OrderStatus(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
}
