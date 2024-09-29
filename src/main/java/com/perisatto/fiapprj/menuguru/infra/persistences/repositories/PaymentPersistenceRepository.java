package com.perisatto.fiapprj.menuguru.infra.persistences.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.perisatto.fiapprj.menuguru.infra.persistences.entities.PaymentDocument;

public interface PaymentPersistenceRepository extends MongoRepository<PaymentDocument, String >{

}
