package com.company.service;


import com.company.exception.TransactionNotFoundException;
import com.company.model.CreateTransaction;
import com.company.model.Transaction;
import com.company.model.UpdateTransaction;
import com.company.repository.TransactionEntity;
import com.company.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@SpringBootTest
public class TransactionServiceTest {

    private static final Long TRANSACTION_ID = 1L;
    private static final String USER_ID = "A1";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(10.10);
    private static final Instant CREATED = Instant.now();
    private static final Instant SYSTEM_INSTANT = ZonedDateTime.now(ZoneOffset.UTC).minusYears(5).toInstant();

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private Clock clock;

    @Test
    public void shouldReturnExisingTransaction() {
        TransactionEntity existingTransaction = TransactionEntity.builder()
                .id(TRANSACTION_ID).userId(USER_ID).amount(AMOUNT).created(CREATED)
                .build();

        Mockito.when(transactionRepository.findById(TRANSACTION_ID))
                .thenReturn(Optional.of(existingTransaction));

        Transaction transaction = transactionService.get(TRANSACTION_ID);

        Assertions.assertEquals(TRANSACTION_ID, transaction.getId());
        Assertions.assertEquals(USER_ID, transaction.getUserId());
        Assertions.assertEquals(AMOUNT, transaction.getAmount());
        Assertions.assertEquals(CREATED, transaction.getCreated());
    }

    @Test
    public void shouldThrowExceptionOnNotExisingTransaction() {
        Mockito.when(transactionRepository.findById(TRANSACTION_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(TransactionNotFoundException.class,
                () -> transactionService.get(TRANSACTION_ID));
    }

    @Test
    public void shouldSuccessfullyCreateTransaction() {
        CreateTransaction createTransaction = CreateTransaction.builder()
                .userId(USER_ID).amount(AMOUNT).created(CREATED)
                .build();

        Mockito.when(transactionRepository.save(any()))
                .thenAnswer(invocation -> TransactionEntity.builder()
                        .id(TRANSACTION_ID).build()
                );

        Long transactionId = transactionService.create(createTransaction);

        Assertions.assertEquals(TRANSACTION_ID, transactionId);
    }

    @Test
    public void shouldSuccessfullyCreateTransactionWithoutSpecifiedTime() {
        CreateTransaction createTransaction = CreateTransaction.builder()
                .userId(USER_ID).amount(AMOUNT)
                .build();

        Mockito.when(clock.instant())
                .thenReturn(SYSTEM_INSTANT);
        Mockito.when(transactionRepository.save(any()))
                .thenReturn(TransactionEntity.builder()
                        .id(TRANSACTION_ID).build()
                );

        Long transactionId = transactionService.create(createTransaction);

        Assertions.assertEquals(TRANSACTION_ID, transactionId);
        Mockito.verify(transactionRepository).save(
                argThat((TransactionEntity arg) -> arg.getCreated().equals(SYSTEM_INSTANT)));
    }

    @Test
    public void shouldSuccessfullyUpdateTransaction() {
        UpdateTransaction updateTransaction = UpdateTransaction.builder()
                .id(TRANSACTION_ID).userId(USER_ID).amount(AMOUNT).created(CREATED)
                .build();

        Mockito.when(transactionRepository.findById(any()))
                .thenAnswer(invocation -> Optional.of(TransactionEntity.builder()
                        .id(invocation.getArgument(0)).build())
                );
        Mockito.when(transactionRepository.save(any()))
                .thenAnswer(invocation -> TransactionEntity.builder()
                        .id(invocation.getArgument(0, TransactionEntity.class).getId()).build()
                );

        Long transactionId = transactionService.update(updateTransaction);

        Assertions.assertEquals(TRANSACTION_ID, transactionId);
    }

    @Test
    public void shouldSuccessfullyUpdateTransactionWithoutSpecifiedTime() {
        UpdateTransaction updateTransaction = UpdateTransaction.builder()
                .id(TRANSACTION_ID).userId(USER_ID).amount(AMOUNT)
                .build();

        Mockito.when(clock.instant())
                .thenReturn(SYSTEM_INSTANT);
        Mockito.when(transactionRepository.findById(any()))
                .thenAnswer(invocation -> Optional.of(TransactionEntity.builder()
                        .id(invocation.getArgument(0)).build())
                );
        Mockito.when(transactionRepository.save(any()))
                .thenAnswer(invocation -> TransactionEntity.builder()
                        .id(invocation.getArgument(0, TransactionEntity.class).getId()).build()
                );

        Long transactionId = transactionService.update(updateTransaction);

        Assertions.assertEquals(TRANSACTION_ID, transactionId);
        Mockito.verify(transactionRepository).save(
                argThat((TransactionEntity arg) -> arg.getCreated().equals(SYSTEM_INSTANT)));
    }

    @Test
    public void shouldThrowExceptionOnUpdateNotExistingTransaction() {
        UpdateTransaction updateTransaction = UpdateTransaction.builder()
                .id(TRANSACTION_ID).userId(USER_ID).amount(AMOUNT).created(CREATED)
                .build();

        Mockito.when(transactionRepository.findById(any()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(TransactionNotFoundException.class,
                () -> transactionService.update(updateTransaction));
    }

    @Test
    public void shouldSuccessfullyDeleteTransaction() {
        Mockito.when(transactionRepository.existsById(TRANSACTION_ID))
                .thenReturn(true);

        transactionService.delete(TRANSACTION_ID);

        Mockito.verify(transactionRepository, Mockito.times(1))
                .deleteById(TRANSACTION_ID);
    }

    @Test
    public void shouldThrowExceptionOnDeleteNotExistingTransaction() {
        Mockito.when(transactionRepository.existsById(TRANSACTION_ID))
                .thenReturn(false);

        Assertions.assertThrows(TransactionNotFoundException.class,
                () -> transactionService.delete(TRANSACTION_ID));

        Mockito.verify(transactionRepository, Mockito.never())
                .deleteById(TRANSACTION_ID);
    }

}
