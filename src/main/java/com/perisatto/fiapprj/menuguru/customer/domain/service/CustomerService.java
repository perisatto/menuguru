package com.perisatto.fiapprj.menuguru.customer.domain.service;

import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.menuguru.customer.domain.model.CPF;
import com.perisatto.fiapprj.menuguru.customer.domain.model.Customer;
import com.perisatto.fiapprj.menuguru.customer.port.in.ManageCustomerUseCase;
import com.perisatto.fiapprj.menuguru.customer.port.out.ManageCustomerPort;
import com.perisatto.fiapprj.menuguru.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.menuguru.handler.exceptions.ValidationException;

public class CustomerService implements ManageCustomerUseCase {

	static final Logger logger = LogManager.getLogger(CustomerService.class);

	private ManageCustomerPort manageCustomerPort;

	public CustomerService(ManageCustomerPort manageCustomerPort) {
		this.manageCustomerPort = manageCustomerPort;
	}

	public Customer createCustomer(String documentNumber, String name, String email) throws Exception {
		logger.info("Asked for new customer register...");
		Customer newCustomer;

		CPF newCustomerDocumentNumber = new CPF(documentNumber);
		String newCustomerName = name;
		String newCustomerEmail = email;

		newCustomer = new Customer(null, newCustomerDocumentNumber, newCustomerName, newCustomerEmail);

		if(manageCustomerPort.getCustomerByCPF(newCustomer.getDocumentNumber()).isEmpty()) {
			logger.info("Customer inexistent... creating new customer register.");
			newCustomer = manageCustomerPort.createCustomer(newCustomer);
			logger.info("New customer register created.");
			return newCustomer;
		}else {
			logger.info("Customer already exists. New customer register aborted.");
			throw new ValidationException("cstm-1001", "Customer already exists");
		}	
	}

	public Customer getCustomerByCPF(String documentNumber) throws Exception {
		CPF customerDocument;
		customerDocument = new CPF(documentNumber);			
		Optional<Customer> customer = manageCustomerPort.getCustomerByCPF(customerDocument);
		if(customer.isPresent()) {
			return customer.get();
		} else {
			throw new NotFoundException("cstm-1003", "Customer not found");
		}	
	}

	public Customer getCustomerById(Long customerId) throws Exception {	
		Optional<Customer> customer = manageCustomerPort.getCustomerById(customerId);
		if(customer.isPresent()) {
			return customer.get();
		} else {
			throw new NotFoundException("cstm-1004", "Customer not found");
		}
	}

	@Override
	public Customer updateCustomer(Long customerId, String documentNumber, String customerName, String customerEmail) throws Exception {	
		Optional<Customer> oldCustomerData = manageCustomerPort.getCustomerById(customerId);
		if(oldCustomerData.isPresent()) {
			
			CPF newDocumentNumber;
			String newCustomerName;
			String newCustomerEmail;
			
			if(documentNumber != null) {
				newDocumentNumber = new CPF(documentNumber);
			} else {
				newDocumentNumber = oldCustomerData.get().getDocumentNumber();
			}
			
			if(customerName != null) {
				newCustomerName = customerName;
			} else {
				newCustomerName = oldCustomerData.get().getName();
			}

			if(customerEmail != null) {
				newCustomerEmail = customerEmail;
			} else {
				newCustomerEmail = oldCustomerData.get().getEmail();
			}
			
			Customer newCustomerData = new Customer(customerId, newDocumentNumber, newCustomerName, newCustomerEmail);
			
			Optional<Customer> updatedCustomerData = manageCustomerPort.updateCustomer(newCustomerData);
			if(updatedCustomerData.isEmpty()) {
				logger.error("Error while updating customer data... customer not found during update action.");
				throw new Exception("Error while updating customer data. Please refer to application log for details.");
			}
			return updatedCustomerData.get();
		}else {
			throw new NotFoundException("cstm-1004", "Customer not found");
		}
	}

	@Override
	public Boolean deleteCustomer(Long customerId) throws Exception {
		Boolean deleted = false;
		Optional<Customer> customer = manageCustomerPort.getCustomerById(customerId);		
		if(customer.isPresent()) {
			deleted = manageCustomerPort.deleteCustomer(customerId);
			logger.warn("Customer register deleted: id=" + customer.get().getId() + " | documentNumber=" + customer.get().getDocumentNumber().getDocumentNumber());
		} else {
			throw new NotFoundException("cstm-1005", "Customer not found");
		}
		return deleted;
	}

	@Override
	public Set<Customer> findAllCustomers(Integer limit, Integer page) throws Exception {
		if(limit==null) {
			limit = 10;
		}
		
		if(page==null) {
			page = 1;
		}
		
		validateFindAll(limit, page);		
		
		Set<Customer> findResult = manageCustomerPort.findAll(limit, page - 1);		
		return findResult;
	}	
	
	private void validateFindAll(Integer limit, Integer page) throws Exception {
		if (limit < 0 || limit > 50) {
			String message = "Invalid size parameter. Value must be greater than 0 and less than 50. Actual value: " + limit;
			logger.debug("\"validateFindAll\" | limit validation: " + message);
			throw new ValidationException("cstm-1006", message);			
		}
		
		if (page < 1) {
			String message = "Invalid page parameter. Value must be greater than 0. Actual value: " + page;
			logger.debug("\"validateFindAll\" | offset validation: " + message);
			throw new ValidationException("cstm-1007", message);	
		}
	}
}
