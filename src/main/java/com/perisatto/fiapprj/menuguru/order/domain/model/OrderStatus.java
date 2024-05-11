package com.perisatto.fiapprj.menuguru.order.domain.model;

public enum OrderStatus {
	RECEBIDO(1L),
	EM_PREPARACAO(2L),
	PRONTO(3L),
	FINALIZADO(4L);
	
	private Long id;
	
	OrderStatus(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
}
