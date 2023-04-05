package com.company.service;

import com.company.exception.TransactionNotFoundException;
import com.company.model.CreateTransaction;
import com.company.model.Transaction;
import com.company.model.UpdateTransaction;
import com.company.repository.TransactionEntity;
import com.company.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository repository;
    private Clock clock;

    public Transaction get(long id) {
        return repository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction " + id + " not found"));
    }

    public Long create(CreateTransaction createTransaction) {
        return repository.save(map(createTransaction)).getId();
    }

    public Long update(UpdateTransaction transaction) {
        TransactionEntity entity = repository.findById(transaction.getId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction " + transaction.getId() + " not found"));
        entity.setUserId(transaction.getUserId());
        entity.setAmount(transaction.getAmount());
        entity.setCreated(orSystemInstant(transaction.getCreated()));
        return repository.save(entity).getId();
    }

    public void delete(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new TransactionNotFoundException("Transaction " + id + " not found");
        }
    }

    private Transaction map(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .amount(entity.getAmount())
                .created(entity.getCreated())
                .build();
    }

    private TransactionEntity map(CreateTransaction transaction) {
        return TransactionEntity.builder()
                .userId(transaction.getUserId())
                .amount(transaction.getAmount())
                .created(orSystemInstant(transaction.getCreated()))
                .build();
    }

    private Instant orSystemInstant(Instant instant) {
        return Optional.ofNullable(instant).orElse(clock.instant());
    }
}
