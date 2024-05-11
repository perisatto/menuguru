package com.perisatto.fiapprj.menuguru.product.adapter.out;

import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.perisatto.fiapprj.menuguru.product.adapter.out.repository.ProductJpaEntity;
import com.perisatto.fiapprj.menuguru.product.adapter.out.repository.ProductRepository;
import com.perisatto.fiapprj.menuguru.product.domain.model.Product;
import com.perisatto.fiapprj.menuguru.product.domain.model.ProductType;
import com.perisatto.fiapprj.menuguru.product.port.out.ManageProductPort;

@Component
public class ProductPersistenceAdapter implements ManageProductPort {
	
	private ProductRepository productRepository;
	
	public ProductPersistenceAdapter(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Product createProduct(Product product) throws Exception {		
		ProductMapper productMapper = new ProductMapper();
		ProductJpaEntity productJpaEntity =  productMapper.mapToJpaEntity(product);
		productJpaEntity.setIdProductStatus(1L);
		productJpaEntity = productRepository.save(productJpaEntity);
		Product newProduct;
		newProduct = productMapper.mapToDomainEntity(productJpaEntity);
		return newProduct;
	}

	@Override
	public Optional<Product> getProductById(Long id) throws Exception {
		Product product;

		Optional<ProductJpaEntity> productJpaEntity = productRepository.findByIdProductAndIdProductStatus(id, 1L);
		if(productJpaEntity.isPresent()) {
			ProductMapper productMapper = new ProductMapper();				
			product = productMapper.mapToDomainEntity(productJpaEntity.get());
		}else {
			return Optional.empty();
		}

		return Optional.of(product);
	}

	@Override
	public Optional<Product> updateProduct(Product product) throws Exception {
		ProductMapper productMapper = new ProductMapper();
		ProductJpaEntity productJpaEntity = productMapper.mapToJpaEntity(product);
		productJpaEntity.setIdProductStatus(1L);
		productJpaEntity = productRepository.save(productJpaEntity);
		Product updatedProduct = productMapper.mapToDomainEntity(productJpaEntity);
		return Optional.of(updatedProduct);
	}

	@Override
	public Boolean deleteProduct(Long id) throws Exception {
		Optional<ProductJpaEntity> product = productRepository.findById(id);
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
		Page<ProductJpaEntity> products;
		
		if(type != null) {		
			productType = ProductType.valueOf(type);
			products = productRepository.findByIdProductTypeAndIdProductStatus(productType.getId(), 1L, pageable);
		} else {
			products = productRepository.findByIdProductStatus(1L, pageable);
		}
		
		Set<Product> productSet = new LinkedHashSet<Product>();
		
		for (ProductJpaEntity product : products) {
			String base64Image = Base64.getEncoder().encodeToString(product.getImage());
			Product retrievedProduct = new Product(product.getName(), ProductType.values()[(int) (product.getIdProductType() - 1)], product.getDescription(), product.getPrice(), base64Image);
			retrievedProduct.setId(product.getIdProduct());
			productSet.add(retrievedProduct);
		}
		return productSet;
	}

}
