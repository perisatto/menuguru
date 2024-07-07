package com.perisatto.fiapprj.menuguru.application.usecases;

import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.menuguru.application.interfaces.ProductRepository;
import com.perisatto.fiapprj.menuguru.domain.entities.product.Product;
import com.perisatto.fiapprj.menuguru.domain.entities.product.ProductType;
import com.perisatto.fiapprj.menuguru.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.menuguru.handler.exceptions.ValidationException;

public class ProductUseCase {

	static final Logger logger = LogManager.getLogger(ProductUseCase.class);
	private final ProductRepository productRepository;
	
	public ProductUseCase(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product createProduct(String name, String type, String description, Double price, String image)	throws Exception {
		Product product;
		try {
			logger.info("Asked for new product register...");
			product = new Product(name, ProductType.valueOf(type), description, price, image);
			product = productRepository.createProduct(product);
			logger.info("New product register created.");
		} catch (IllegalArgumentException e) {
			if(e.getMessage().contains("No enum constant")) {
				throw new ValidationException("prdt-2001","Invalid product type");
			}else {
				logger.info("Error creating a new product: "+ e.getMessage());
				throw e;
			}
		} catch (NullPointerException e) {
			logger.info("Error creating a new product: "+ e.getMessage());
			throw new ValidationException("prdt-2001","Invalid product type");
		}
		return product;
	}

	public Product getProduct(Long id) throws Exception {
		Optional<Product> product = productRepository.getProductById(id);
		if(product.isPresent()) {
			return product.get();
		} else {
			throw new NotFoundException("prdt-2002", "Product not found");
		}
	}

	public Product updateProduct(Long id, String name, String type, String description, Double price, String image) throws Exception {
		Optional<Product> oldProductData = productRepository.getProductById(id);
		if(oldProductData.isPresent()) {

			String newName;
			ProductType newType;
			String newDescription;
			Double newPrice;
			String newImage;

			if(name != null) {
				newName = name;
			} else {
				newName = oldProductData.get().getName();
			}

			if(type != null) {
				newType = ProductType.valueOf(type);
			} else {
				newType = oldProductData.get().getProductType();
			}

			if(description != null) {
				newDescription = description;
			} else {
				newDescription = oldProductData.get().getDescription();
			}

			if(price != null) {
				newPrice = price;
			} else {
				newPrice = oldProductData.get().getPrice();
			}

			if(image != null) {
				newImage = image;
			} else {
				newImage = oldProductData.get().getImage();
			}

			Product newProductData = new Product(newName, newType, newDescription, newPrice, newImage);
			newProductData.setId(id);

			Optional<Product> updatedProductData = productRepository.updateProduct(newProductData);
			if(updatedProductData.isEmpty()) {
				logger.error("Error while updating product data... product not found during update action.");
				throw new Exception("Error while updating product data. Please refer to application log for details.");
			}
			return updatedProductData.get();
		}else {
			throw new NotFoundException("prdt-2006", "Product not found");
		}	
	}

	public Boolean deleteProduct(Long id) throws Exception {
		Boolean deleted = false;
		Optional<Product> product = productRepository.getProductById(id);		
		if(product.isPresent()) {
			deleted = productRepository.deleteProduct(id);
			logger.warn("Product register deleted: id=" + product.get().getId());
		} else {
			throw new NotFoundException("prdt-2005", "Product not found");
		}
		return deleted;
	}

	public Set<Product> findAllProducts(Integer limit, Integer page, String type) throws Exception {

		try {	
			if(limit==null) {
				limit = 10;
			}

			if(page==null) {
				page = 1;
			}

			validateFindAll(limit, page);		

			Set<Product> findResult = productRepository.findAll(limit, page - 1, type);		
			return findResult;
		} catch (IllegalArgumentException e) {
			if(e.getMessage().contains("No enum constant")) {
				throw new ValidationException("prdt-2007","Invalid product type");
			}else {
				logger.info("Error getting products: "+ e.getMessage());
				throw e;
			}
		} catch (NullPointerException e) {
			logger.info("Error getting products: "+ e.getMessage());
			throw new ValidationException("prdt-2008","Invalid product type");
		}
	}

	private void validateFindAll(Integer limit, Integer page) throws Exception {
		if (limit < 0 || limit > 50) {
			String message = "Invalid size parameter. Value must be greater than 0 and less than 50. Actual value: " + limit;
			logger.debug("\"validateFindAll\" | limit validation: " + message);
			throw new ValidationException("prdt-2003", message);			
		}

		if (page < 1) {
			String message = "Invalid page parameter. Value must be greater than 0. Actual value: " + page;
			logger.debug("\"validateFindAll\" | offset validation: " + message);
			throw new ValidationException("prdt-2004", message);	
		}
	}
}
