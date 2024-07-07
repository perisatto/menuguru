package com.perisatto.fiapprj.menuguru.domain.entities.product;

public enum ProductType {
	LANCHE(1L),
	ACOMPANHAMENTO(2L),
	BEBIDA(3L),
	SOBREMESA(4L);
	
	private Long id;

	ProductType(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
}
