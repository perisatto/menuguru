package com.perisatto.fiapprj.menuguru.product.adapter.out;

import java.util.Base64;

import com.perisatto.fiapprj.menuguru.product.adapter.out.repository.ProductJpaEntity;
import com.perisatto.fiapprj.menuguru.product.domain.model.Product;
import com.perisatto.fiapprj.menuguru.product.domain.model.ProductType;

public class ProductMapper {	
	Product mapToDomainEntity(ProductJpaEntity product) throws Exception {
		String base64Image = Base64.getEncoder().encodeToString(product.getImage());		
		Product productDomainEntity = new Product(product.getName(), ProductType.values()[(int) (product.getIdProductType() - 1)], product.getDescription(), product.getPrice(), base64Image);
		productDomainEntity.setId(product.getIdProduct());
		return productDomainEntity;
	}
	
	ProductJpaEntity mapToJpaEntity(Product product) {		
		ProductJpaEntity productJpaEntity = new ProductJpaEntity();
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
