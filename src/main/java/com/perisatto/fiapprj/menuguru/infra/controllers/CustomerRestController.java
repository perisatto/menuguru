package com.perisatto.fiapprj.menuguru.infra.controllers;

import java.net.URI;
import java.util.Properties;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perisatto.fiapprj.menuguru.application.usecases.CustomerUseCase;
import com.perisatto.fiapprj.menuguru.domain.entities.customer.Customer;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.CreateCustomerRequestDTO;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.CreateCustomerResponseDTO;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.GetCustomerListResponseDTO;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.GetCustomerResponseDTO;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.UpdateCustomerRequestDTO;

@RestController
@RequestMapping("/menuguru/v1")
public class CustomerRestController {
	
	private final CustomerUseCase customerUseCase;
	private final Properties requestProperties;

	public CustomerRestController(CustomerUseCase customerUseCase, Properties requestProperties) {
		this.customerUseCase = customerUseCase;
		this.requestProperties = requestProperties;
	}
	
	@PostMapping(value = "/customers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateCustomerResponseDTO> createCustomer(@RequestBody CreateCustomerRequestDTO createRequest) throws Exception {
		requestProperties.setProperty("resourcePath", "/customers");
		
		Customer customer = customerUseCase.createCustomer(createRequest.getDocumentNumber(), createRequest.getName(), createRequest.getEmail());		
		ModelMapper customerMapper = new ModelMapper();
		CreateCustomerResponseDTO response = customerMapper.map(customer, CreateCustomerResponseDTO.class);
		URI location = new URI("/customers/" + response.getId());
		return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
	}
	
	@GetMapping(value = "/customersByCPF/{documentNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetCustomerResponseDTO> get(@PathVariable(value = "documentNumber") String documentNumber) throws Exception {
		requestProperties.setProperty("resourcePath", "/customersByCPF/" + documentNumber);
		Customer customer = customerUseCase.getCustomerByCPF(documentNumber);
		ModelMapper customerMapper = new ModelMapper();
		GetCustomerResponseDTO response = customerMapper.map(customer, GetCustomerResponseDTO.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/customers/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetCustomerResponseDTO> get(@PathVariable(value = "customerId") Long customerId) throws Exception {
		requestProperties.setProperty("resourcePath", "/customers/" + customerId.toString());
		Customer customer = customerUseCase.getCustomerById(customerId);
		ModelMapper customerMapper = new ModelMapper();
		GetCustomerResponseDTO response = customerMapper.map(customer, GetCustomerResponseDTO.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetCustomerListResponseDTO> getAll(@RequestParam(value = "_page", required = true) Integer page, @RequestParam(value = "_size", required = true) Integer size) throws Exception {
		requestProperties.setProperty("resourcePath", "/customers");
		Set<Customer> customer = customerUseCase.findAllCustomers(size, page);
		GetCustomerListResponseDTO response = new GetCustomerListResponseDTO();
		response.setContent(customer, page, size);		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PatchMapping(value = "/customers/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetCustomerResponseDTO> patch(@PathVariable(value = "customerId") Long customerId, @RequestBody UpdateCustomerRequestDTO updateRequest) throws Exception {
		requestProperties.setProperty("resourcePath", "/customers/" + customerId.toString());			
		Customer customer = customerUseCase.updateCustomer(customerId, updateRequest.getDocumentNumber(), updateRequest.getName(), updateRequest.getEmail());
		ModelMapper customerMapper = new ModelMapper();
		GetCustomerResponseDTO response = customerMapper.map(customer, GetCustomerResponseDTO.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping(value = "/customers/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> delete(@PathVariable(value = "customerId") Long customerId) throws Exception {
		requestProperties.setProperty("resourcePath", "/customers/" + customerId.toString());			
		customerUseCase.deleteCustomer(customerId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
}
