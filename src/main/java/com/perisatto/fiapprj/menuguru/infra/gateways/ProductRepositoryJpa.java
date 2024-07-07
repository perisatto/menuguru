package com.perisatto.fiapprj.menuguru.infra.gateways;

import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.perisatto.fiapprj.menuguru.application.interfaces.ProductRepository;
import com.perisatto.fiapprj.menuguru.domain.entities.product.Product;
import com.perisatto.fiapprj.menuguru.domain.entities.product.ProductType;
import com.perisatto.fiapprj.menuguru.infra.gateways.mappers.ProductMapper;
import com.perisatto.fiapprj.menuguru.infra.persistences.entities.ProductEntity;
import com.perisatto.fiapprj.menuguru.infra.persistences.repositories.ProductPersistenceRepository;

public class ProductRepositoryJpa implements ProductRepository {
	
	private final ProductPersistenceRepository productRepository;
	private final ProductMapper productMapper;

	public ProductRepositoryJpa(ProductPersistenceRepository productRepository, ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.productMapper = productMapper;
	}

	@Override
	public Product createProduct(Product product) throws Exception {		
		ProductEntity ProductEntity =  productMapper.mapToJpaEntity(product);
		ProductEntity.setIdProductStatus(1L);
		ProductEntity = productRepository.save(ProductEntity);
		Product newProduct;
		newProduct = productMapper.mapToDomainEntity(ProductEntity);
		return newProduct;
	}

	@Override
	public Optional<Product> getProductById(Long id) throws Exception {
		Product product;

		Optional<ProductEntity> ProductEntity = productRepository.findByIdProductAndIdProductStatus(id, 1L);
		if(ProductEntity.isPresent()) {
			product = productMapper.mapToDomainEntity(ProductEntity.get());
		}else {
			return Optional.empty();
		}

		return Optional.of(product);
	}

	@Override
	public Optional<Product> updateProduct(Product product) throws Exception {
		ProductEntity ProductEntity = productMapper.mapToJpaEntity(product);
		ProductEntity.setIdProductStatus(1L);
		ProductEntity = productRepository.save(ProductEntity);
		Product updatedProduct = productMapper.mapToDomainEntity(ProductEntity);
		return Optional.of(updatedProduct);
	}

	@Override
	public Boolean deleteProduct(Long id) throws Exception {
		Optional<ProductEntity> product = productRepository.findById(id);
		if(product.isPresent()) {
			product.get().setIdProductStatus(2L);
			productRepository.save(product.get());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Set<Product> findAll(Integer limit, Integer offset, String type) throws Exception {	
		
		ProductType productType = null;
		
		Pageable pageable = PageRequest.of(offset, limit, Sort.by("idProduct"));
		Page<ProductEntity> products;
		
		if(type != null) {		
			productType = ProductType.valueOf(type);
			products = productRepository.findByIdProductTypeAndIdProductStatus(productType.getId(), 1L, pageable);
		} else {
			products = productRepository.findByIdProductStatus(1L, pageable);
		}
		
		Set<Product> productSet = new LinkedHashSet<Product>();
		
		for (ProductEntity product : products) {
			String base64Image = Base64.getEncoder().encodeToString(product.getImage());
			Product retrievedProduct = new Product(product.getName(), ProductType.values()[(int) (product.getIdProductType() - 1)], product.getDescription(), product.getPrice(), base64Image);
			retrievedProduct.setId(product.getIdProduct());
			productSet.add(retrievedProduct);
		}
		return productSet;
	}
}
