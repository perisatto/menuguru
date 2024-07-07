package com.perisatto.fiapprj.menuguru.infra.persistences.repositories;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.perisatto.fiapprj.menuguru.infra.persistences.entities.CustomerEntity;

public interface CustomerPersistenceRepository extends JpaRepository<CustomerEntity, Long> {

	Optional<CustomerEntity> findByIdCustomerAndIdCustomerStatus(Long customerId, Long idStatus);

	Optional<CustomerEntity> findByDocumentNumberAndIdCustomerStatus(String documentNumber, Long idStatus);

	Page<CustomerEntity> findByIdCustomerStatus(Long idStatus, Pageable pageable);
}
