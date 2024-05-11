package com.perisatto.fiapprj.menuguru.customer.adapter.out.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Customer")
public class CustomerJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCustomer;
	private String name;
	private String documentNumber;
	private String eMail;
	private Long idCustomerStatus;
	
	
	public Long getIdCustomer() {
		return idCustomer;
	}
	
	public void setIdCustomer(Long idCustomer) {
		this.idCustomer = idCustomer;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDocumentNumber() {
		return documentNumber;
	}
	
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	public String geteMail() {
		return eMail;
	}
	
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public Long getIdCustomerStatus() {
		return idCustomerStatus;
	}

	public void setIdCustomerStatus(Long idCustomerStatus) {
		this.idCustomerStatus = idCustomerStatus;
	}
}
