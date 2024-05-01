package com.perisatto.fiapprj.menuguru.application.domain;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.perisatto.fiapprj.menuguru.application.domain.model.CPF;
import com.perisatto.fiapprj.menuguru.application.domain.model.Customer;

public class CustomerTest {
	
	@Test
	void givenValidCPF_thenRegisterCustomer() throws Exception {
		
		String customerName = "Roberto Machado";
		String customerEmail = "roberto.machado@bestmail.com";
		String documentNumber = "90779778057";
		final CPF customerCPF = new CPF(documentNumber);
		Customer customer = new Customer(customerCPF, customerName, customerEmail);
		
		assertThat(customer.getName()).isEqualTo(customerName);
		assertThat(customer.getEmail()).isEqualTo(customerEmail);
		assertThat(customer.getCPF()).isEqualTo(customerCPF.getDocumentNumber());
	}
	
	@Test
	void givenInvalidCPF_thenRefusesToRegisterCustomer() throws Exception {
		try {
			String customerName = "Roberto Machado";
			String customerEmail = "roberto.machado@bestmail.com";
			String documentNumber = "90779778058";
			
			final CPF customerCPF = new CPF(documentNumber);
			Customer customer = new Customer(customerCPF, customerName, customerEmail);
			
			assertThat(customer.getName()).isNull();
			assertThat(customer.getEmail()).isNull();
			assertThat(customer.getCPF()).isNull();
		}catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("CPF inválido");
		}
	}
}
