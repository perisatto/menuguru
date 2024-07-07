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

import com.perisatto.fiapprj.menuguru.application.usecases.ProductUseCase;
import com.perisatto.fiapprj.menuguru.domain.entities.product.Product;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.CreateProductRequestDTO;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.CreateProductResponseDTO;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.GetProductListResponseDTO;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.GetProductResponseDTO;
import com.perisatto.fiapprj.menuguru.infra.controllers.dtos.UpdateProductRequestDTO;


@RestController
@RequestMapping("/menuguru/v1")
public class ProductRestController {

	private final ProductUseCase productUseCase;
	private final Properties requestProperties;
	
	public ProductRestController(ProductUseCase productUseCase, Properties requestProperties) {
		this.productUseCase = productUseCase;
		this.requestProperties = requestProperties;
	}
	
	@PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateProductResponseDTO> createUser(@RequestBody CreateProductRequestDTO createRequest) throws Exception {
		requestProperties.setProperty("resourcePath", "/products");
		Product product = productUseCase.createProduct(createRequest.getName(), createRequest.getProductType(), 
				createRequest.getDescription(), createRequest.getPrice(), createRequest.getImage());
		ModelMapper productMapper = new ModelMapper();
		CreateProductResponseDTO response = productMapper.map(product, CreateProductResponseDTO.class);
		URI location = new URI("/products/" + response.getId());
		return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
	}
	
	@GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetProductListResponseDTO> getAll(@RequestParam(value = "_page", required = true) Integer page, 
			@RequestParam(value = "_size", required = true) Integer size, @RequestParam(value = "type", required=false) String type) throws Exception {
		requestProperties.setProperty("resourcePath", "/products");
		Set<Product> product = productUseCase.findAllProducts(size, page, type);
		GetProductListResponseDTO response = new GetProductListResponseDTO();
		response.setContent(product, page, size);		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetProductResponseDTO> get(@PathVariable(value = "productId") Long productId) throws Exception {
		requestProperties.setProperty("resourcePath", "/products/" + productId.toString());
		Product product = productUseCase.getProduct(productId);
		ModelMapper productMapper = new ModelMapper();
		GetProductResponseDTO response = productMapper.map(product, GetProductResponseDTO.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> delete(@PathVariable(value = "productId") Long productId) throws Exception {
		requestProperties.setProperty("resourcePath", "/products/" + productId.toString());			
		productUseCase.deleteProduct(productId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	@PatchMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetProductResponseDTO> patch(@PathVariable(value = "productId") Long productId, @RequestBody UpdateProductRequestDTO updateRequest) throws Exception {
		requestProperties.setProperty("resourcePath", "/products/" + productId.toString());			
		Product product = productUseCase.updateProduct(productId, updateRequest.getName(), updateRequest.getProductType(), 
				updateRequest.getDescription(), updateRequest.getPrice(), updateRequest.getImage());
		ModelMapper productMapper = new ModelMapper();
		GetProductResponseDTO response = productMapper.map(product, GetProductResponseDTO.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
