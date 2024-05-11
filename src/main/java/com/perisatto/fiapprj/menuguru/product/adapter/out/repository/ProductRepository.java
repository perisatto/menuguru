package com.perisatto.fiapprj.menuguru.product.adapter.out.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductJpaEntity, Long> {

	Page<ProductJpaEntity> findByIdProductType(Long id, Pageable pageable);

	Page<ProductJpaEntity> findByIdProductTypeAndIdProductStatus(Long id, Long idStatus, Pageable pageable);

	Page<ProductJpaEntity> findByIdProductStatus(Long idStatus, Pageable pageable);

	Optional<ProductJpaEntity> findByIdProductAndIdProductStatus(Long id, Long idStatus);

}
