package com.company.repository;

import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {

    List<TransactionEntity> findAllByCreatedAfterAndAmountGreaterThan(Instant fromCreated, BigDecimal minimalAmount);
}
