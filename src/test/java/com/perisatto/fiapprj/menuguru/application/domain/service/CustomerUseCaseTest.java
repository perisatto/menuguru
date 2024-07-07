package com.perisatto.fiapprj.menuguru.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.menuguru.application.interfaces.CustomerRepository;
import com.perisatto.fiapprj.menuguru.application.usecases.CustomerUseCase;
import com.perisatto.fiapprj.menuguru.domain.entities.customer.CPF;
import com.perisatto.fiapprj.menuguru.domain.entities.customer.Customer;
import com.perisatto.fiapprj.menuguru.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.menuguru.handler.exceptions.ValidationException;

@ActiveProfiles(value = "test")
public class CustomerUseCaseTest {

	private CustomerRepository manageCustomerPortUpdate = new CustomerRepository() {

		@Override
		public Customer createCustomer(Customer customer) {
			try {
				Customer newCustomer = new Customer(10L, customer.getDocumentNumber(), customer.getName(), customer.getEmail());
				return newCustomer;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public Optional<Customer> getCustomerById(Long customerId) throws Exception {
			if(customerId == 10L) {	
				CPF documentNumber = new CPF("65678860054");
				Customer customer = new Customer(10L, documentNumber, "Roberto Facao", "roberto.facao@bestmail.com");
				return Optional.of(customer);
			} else {
				return Optional.empty();
			}
		}

		@Override
		public Optional<Customer> getCustomerByCPF(CPF customerDocument) throws Exception {
			try {
				CPF documentNumber = new CPF("65678860054");
				if(documentNumber.getDocumentNumber() == customerDocument.getDocumentNumber()) {					
					Customer customer = new Customer(10L, documentNumber, "Roberto Facao", "roberto.facao@bestmail.com");
					return Optional.of(customer);
				} else {
					return Optional.empty();
				}
			} catch (Exception e) {
				return Optional.empty();
			}
		}

		@Override
		public Optional<Customer> updateCustomer(Customer customer) throws Exception {
			return Optional.of(customer);
		}

		@Override
		public Boolean deleteCustomer(Long customerId) throws Exception {
			if(customerId == 10L) {
				return true;
			} else {
				throw new NotFoundException(null, "Customer not found");
			}
		}

		@Override
		public Set<Customer> findAll(Integer limit, Integer offset) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	};

	private final CustomerRepository manageCustomerPort = new CustomerRepository() {

		@Override
		public Customer createCustomer(Customer customer) {
			try {
				Customer newCustomer = new Customer(10L, customer.getDocumentNumber(), customer.getName(), customer.getEmail());
				return newCustomer;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public Optional<Customer> getCustomerByCPF(CPF customerDocument) {						
			try {
				CPF documentNumber = new CPF("90779778057");
				if(documentNumber.getDocumentNumber() == customerDocument.getDocumentNumber()) {					
					Customer customer = new Customer(10L, documentNumber, "Roberto Machado", "roberto.machado@bestmail.com");
					return Optional.of(customer);
				} else {
					return Optional.empty();
				}
			} catch (Exception e) {
				return Optional.empty();
			}
		}

		@Override
		public Optional<Customer> getCustomerById(Long customerId) throws Exception {
			if(customerId == 10L) {	
				CPF documentNumber = new CPF("90779778057");
				Customer customer = new Customer(10L, documentNumber, "Roberto Machado", "roberto.machado@bestmail.com");
				return Optional.of(customer);
			} else {
				return Optional.empty();
			}
		}

		@Override
		public Optional<Customer> updateCustomer(Customer customer) throws Exception {
			return Optional.of(customer);
		}

		@Override
		public Boolean deleteCustomer(Long customerId) throws Exception {
			if(customerId == 10L) {
				return true;
			} else {
				throw new NotFoundException(null, "Customer not found");
			}
		}

		@Override
		public Set<Customer> findAll(Integer limit, Integer offset) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	};


	@Test
	void givenValidCPF_thenRegisterCustomer() throws Exception {		
		String customerName = "Roberto Machado";
		String customerEmail = "roberto.machado@bestmail.com";
		String documentNumber = "35732996010";

		CustomerUseCase newCustomerUseCase = new CustomerUseCase(manageCustomerPort);
		
		Customer customer = newCustomerUseCase.createCustomer(documentNumber, customerName, customerEmail);

		assertThat(customer.getDocumentNumber().getDocumentNumber()).isEqualTo(documentNumber);
		assertThat(customer.getName()).isEqualTo(customerName);
		assertThat(customer.getEmail()).isEqualTo(customerEmail);
	}

	@Test
	void givenInvalidCPF_thenRefusesToRegisterCustomer() {

		String customerName = "Roberto Machado";
		String customerEmail = "roberto.machado@bestmail.com";
		String documentNumber = "90779778058";

		CustomerUseCase newCustomerUseCase = new CustomerUseCase(manageCustomerPort);


		try {
			Customer customer = newCustomerUseCase.createCustomer(documentNumber, customerName, customerEmail);
		} catch (ValidationException e) {
			assertThat(e.getMessage()).isEqualTo("Invalid document number");
		} catch (Exception e) {
			assertThat(e.getMessage()).isNotEqualTo("Invalid document number");
		}
	}

	@Test
	void givenInvalidEmail_thenRefusesToRegisterCustomer() {

		String customerName = "Roberto Machado";
		String customerEmail = "roberto.machadobestmail.com";
		String documentNumber = "90779778057";

		CustomerUseCase newCustomerUseCase = new CustomerUseCase(manageCustomerPort);

		try {
			Customer customer = newCustomerUseCase.createCustomer(documentNumber, customerName, customerEmail);
		} catch (ValidationException e) {
			assertThat(e.getMessage()).contains("invalid e-mail format");
		} catch (Exception e) {
			assertThat(e.getMessage()).doesNotContain("invalid e-mail format");
		}
	}

	@Test
	void givenEmptyName_thenRefusesToRegisterCustomer() throws Exception {

		String customerName = "";
		String customerEmail = "roberto.machadobestmail.com";
		String documentNumber = "90779778057";

		CustomerUseCase newCustomerUseCase = new CustomerUseCase(manageCustomerPort);

		try {
			Customer customer = newCustomerUseCase.createCustomer(documentNumber, customerName, customerEmail);
		} catch (ValidationException e) {
			assertThat(e.getMessage()).contains("empty, null or blank name");
		} catch (Exception e) {
			assertThat(e.getMessage()).doesNotContain("empty, null or blank name");
		}
	}

	@Test
	void givenCPF_thenGetCustomerData() {
		try {
			String documentNumber = "90779778057";

			CustomerUseCase newCustomerUseCase = new CustomerUseCase(manageCustomerPort);

			Customer customer = newCustomerUseCase.getCustomerByCPF(documentNumber);

			assertThat(customer.getDocumentNumber().getDocumentNumber()).isEqualTo(documentNumber);
		} catch (NotFoundException e) {
			assertThat(e.getMessage()).doesNotContain("Customer not found");
		} catch (Exception e) {
			assertThat(e.getMessage()).doesNotContain("Customer not found");
		}		
	}

	@Test
	void givenInexistentCPF_thenGetNotFound () {
		try {
			String documentNumber = "35732996010";

			CustomerUseCase newCustomerUseCase = new CustomerUseCase(manageCustomerPort);

			Customer customer = newCustomerUseCase.getCustomerByCPF(documentNumber);

			assertThat(customer.getName()).isNullOrEmpty();
		} catch (NotFoundException e) {
			assertThat(e.getMessage()).isEqualTo("Customer not found");
		} catch (Exception e) {
			assertThat(e.getMessage()).doesNotContain("Customer not found");
		}

	}

	@Test
	void givenValidId_thenGetCustomer () {
		CustomerUseCase newCustomerUseCase = new CustomerUseCase(manageCustomerPort);

		Long customerId = 10L;

		try {
			Customer customer = newCustomerUseCase.getCustomerById(customerId);

			assertThat(customer.getId()).isEqualTo(customerId);
		} catch (ValidationException e) {
			assertThat(e.getMessage()).doesNotContain("Customer not found");
		} catch (Exception e) {
			assertThat(e.getMessage()).doesNotContain("Customer not found");
		}
	}

	@Test
	void giveInexistentId_thenGetCustomerNotFound () {
		try {
			CustomerUseCase newCustomerUseCase = new CustomerUseCase(manageCustomerPort);

			Long customerId = 20L;

			Customer customer = newCustomerUseCase.getCustomerById(customerId);

			assertThat(customer.getName()).isNullOrEmpty();
		} catch (NotFoundException e) {
			assertThat(e.getMessage()).isEqualTo("Customer not found");
		} catch (Exception e) {
			assertThat(e.getMessage()).doesNotContain("Customer not found");
		}
	}

	@Test
	void givenNewName_thenUpdateName () throws Exception {		
		CustomerUseCase CustomerUseCase = new CustomerUseCase(manageCustomerPortUpdate);

		Long customerId = 10L;
		String customerName = "Roberto Facao";
		String customerEmail = "roberto.facao@bestmail.com";
		String documentNumber = "65678860054";

		Customer customer = CustomerUseCase.getCustomerById(customerId); 

		Customer newCustomerData = CustomerUseCase.updateCustomer(customerId, documentNumber, customerName, customerEmail);

		customer = CustomerUseCase.getCustomerById(customerId);

		assertThat(customer.getId()).isEqualTo(newCustomerData.getId());
		assertThat(customer.getDocumentNumber().getDocumentNumber()).isEqualTo(newCustomerData.getDocumentNumber().getDocumentNumber());
		assertThat(customer.getName()).isEqualTo(newCustomerData.getName());
		assertThat(customer.getEmail()).isEqualTo(newCustomerData.getEmail());
	}

	@Test
	void givenNewEmail_thenUpdateEmail () throws Exception {
		CustomerUseCase CustomerUseCase = new CustomerUseCase(manageCustomerPortUpdate);

		Long customerId = 10L;
		String customerName = "Roberto Facao";
		String customerEmail = "roberto.facao@bestmail.com";
		String documentNumber = "65678860054";

		Customer customer = CustomerUseCase.getCustomerById(customerId); 

		Customer newCustomerData = CustomerUseCase.updateCustomer(customerId, documentNumber, customerName, customerEmail);

		customer = CustomerUseCase.getCustomerById(customerId);

		assertThat(customer.getId()).isEqualTo(newCustomerData.getId());
		assertThat(customer.getDocumentNumber().getDocumentNumber()).isEqualTo(newCustomerData.getDocumentNumber().getDocumentNumber());
		assertThat(customer.getName()).isEqualTo(newCustomerData.getName());
		assertThat(customer.getEmail()).isEqualTo(newCustomerData.getEmail());
	}

	@Test
	void givenInvalidNewEmail_thenRefusesUpdateEmail () throws Exception {
		try {
			CustomerUseCase CustomerUseCase = new CustomerUseCase(manageCustomerPortUpdate);

			Long customerId = 10L;
			String customerName = "Roberto Facao";
			String customerEmail = "roberto.facaobestmail.com";
			String documentNumber = "90779778057";

			Customer customer = CustomerUseCase.getCustomerById(customerId); 

			Customer newCustomerData = CustomerUseCase.updateCustomer(customerId, documentNumber, customerName, customerEmail);

			customer = CustomerUseCase.getCustomerById(customerId);
		}catch (Exception e) {
			assertThatExceptionOfType(ValidationException.class);
			assertThat(e.getMessage()).contains("invalid e-mail format");
		}
	}

	@Test
	void givenNewCPF_thenUpdateCPF () throws Exception {
		CustomerUseCase CustomerUseCase = new CustomerUseCase(manageCustomerPortUpdate);

		Long customerId = 10L;
		String customerName = "Roberto Facao";
		String customerEmail = "roberto.facao@bestmail.com";
		String documentNumber = "65678860054";

		Customer customer = CustomerUseCase.getCustomerById(customerId); 

		Customer newCustomerData = CustomerUseCase.updateCustomer(customerId, documentNumber, customerName, customerEmail);

		customer = CustomerUseCase.getCustomerById(customerId);

		assertThat(customer.getId()).isEqualTo(newCustomerData.getId());
		assertThat(customer.getDocumentNumber().getDocumentNumber()).isEqualTo(newCustomerData.getDocumentNumber().getDocumentNumber());
		assertThat(customer.getName()).isEqualTo(newCustomerData.getName());
		assertThat(customer.getEmail()).isEqualTo(newCustomerData.getEmail());
	}

	@Test
	void givenInvalidNewCPF_thenRefusesUpdateCPF () throws Exception {
		try {
			CustomerUseCase CustomerUseCase = new CustomerUseCase(manageCustomerPortUpdate);

			Long customerId = 10L;
			String customerName = "Roberto Machado";
			String customerEmail = "roberto.machado@bestmail.com";
			String documentNumber = "90779778057";

			Customer customer = CustomerUseCase.getCustomerById(customerId); 

			Customer newCustomerData = CustomerUseCase.updateCustomer(customerId, documentNumber, customerName, customerEmail);

			customer = CustomerUseCase.getCustomerById(customerId);
		}catch (Exception e) {
			assertThatExceptionOfType(ValidationException.class);
			assertThat(e.getMessage()).contains("Invalid document number");
		}
	}

	@Test
	void givenId_thenDeleteCustomer () {
		CustomerUseCase CustomerUseCase = new CustomerUseCase(manageCustomerPort);
		Boolean deleted = false;

		try {
			Long customerId = 10L;
			deleted = CustomerUseCase.deleteCustomer(customerId);
			Customer customer = CustomerUseCase.getCustomerById(customerId);

		} catch (NotFoundException e) {
			assertThat(deleted).isTrue();
		} catch (Exception e) {
			assertThatExceptionOfType(Exception.class);
		}
	}

	@Test
	void givenInexistentId_thenRefusesDeleteCustomer () {
		CustomerUseCase CustomerUseCase = new CustomerUseCase(manageCustomerPort);
		Boolean deleted = false;

		try {
			Long customerId = 20L;
			deleted = CustomerUseCase.deleteCustomer(customerId);
		} catch (NotFoundException e) {
			assertThatExceptionOfType(NotFoundException.class);
		} catch (Exception e) {
			assertThatExceptionOfType(Exception.class);
		}
	}
}
