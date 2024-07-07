package com.perisatto.fiapprj.menuguru.infra.gateways.mappers;

import java.util.Base64;

import com.perisatto.fiapprj.menuguru.domain.entities.product.Product;
import com.perisatto.fiapprj.menuguru.domain.entities.product.ProductType;
import com.perisatto.fiapprj.menuguru.infra.persistences.entities.ProductEntity;



public class ProductMapper {	
	public Product mapToDomainEntity(ProductEntity product) throws Exception {
		String base64Image = Base64.getEncoder().encodeToString(product.getImage());		
		Product productDomainEntity = new Product(product.getName(), ProductType.values()[(int) (product.getIdProductType() - 1)], product.getDescription(), product.getPrice(), base64Image);
		productDomainEntity.setId(product.getIdProduct());
		return productDomainEntity;
	}
	
	public ProductEntity mapToJpaEntity(Product product) {		
		ProductEntity productJpaEntity = new ProductEntity();
		productJpaEntity.setIdProduct(product.getId());
		productJpaEntity.setName(product.getName());
		productJpaEntity.setIdProductType(product.getProductType().getId());
		productJpaEntity.setDescription(product.getDescription());
		productJpaEntity.setPrice(product.getPrice());
		
		byte[] imageFile = Base64.getDecoder().decode(product.getImage());
		
		productJpaEntity.setImage(imageFile);
		
		return productJpaEntity;
		
	}
}
