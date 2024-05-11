package com.perisatto.fiapprj.menuguru.customer.adapter.out.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerJpaEntity, Long> {
	
	Optional<CustomerJpaEntity> findByIdCustomerAndIdCustomerStatus(Long customerId, Long idStatus);

	Optional<CustomerJpaEntity> findByDocumentNumberAndIdCustomerStatus(String documentNumber, Long idStatus);

	Page<CustomerJpaEntity> findByIdCustomerStatus(Long idStatus, Pageable pageable);
}
