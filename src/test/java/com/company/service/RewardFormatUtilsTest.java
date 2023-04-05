package com.company.service;

import com.company.model.CustomerRewardPoints;
import com.company.model.MonthlyPoints;
import com.company.repository.TransactionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;

public class RewardFormatUtilsTest {

    @Test
    public void checkDoFormatOnSingleTransaction() {

        Function<BigDecimal, Integer> simpleFunction = BigDecimal::intValue;
        List<TransactionEntity> singleTransaction = List.of(
                transactionEntity("A", "100", "2023-03-01T10:00:00.000000000Z")
        );

        List<CustomerRewardPoints> result = RewardFormatUtils.format(singleTransaction, simpleFunction);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("A", result.get(0).getUserId());
        Assertions.assertEquals(3, result.get(0).getMonthlyPoints().get(0).getMonth());
        Assertions.assertEquals(100, result.get(0).getMonthlyPoints().get(0).getPoints());
        Assertions.assertEquals(100, result.get(0).getTotalPoints());
    }

    @Test
    public void checkDoFormatOnTwoDifferentCustomerTransactions() {

        Function<BigDecimal, Integer> simpleFunction = BigDecimal::intValue;
        List<TransactionEntity> singleTransaction = List.of(
                transactionEntity("A", "100", "2023-03-01T10:00:00.000000000Z"),
                transactionEntity("B", "99", "2023-02-28T10:00:00.000000000Z")
        );

        List<CustomerRewardPoints> result = RewardFormatUtils.format(singleTransaction, simpleFunction);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1, result.stream().filter(r -> r.getUserId().equals("A")).count());
        CustomerRewardPoints customerA = result.stream().filter(r -> r.getUserId().equals("A")).findFirst().get();
        Assertions.assertEquals(3, customerA.getMonthlyPoints().get(0).getMonth());
        Assertions.assertEquals(100, customerA.getMonthlyPoints().get(0).getPoints());
        Assertions.assertEquals(100, customerA.getTotalPoints());
        CustomerRewardPoints customerB = result.stream().filter(r -> r.getUserId().equals("B")).findFirst().get();
        Assertions.assertEquals(2, customerB.getMonthlyPoints().get(0).getMonth());
        Assertions.assertEquals(99, customerB.getMonthlyPoints().get(0).getPoints());
        Assertions.assertEquals(99, customerB.getTotalPoints());
    }

    @Test
    public void checkDoFormatOnSingleCustomerWithDifferentMonthsTransactions() {

        Function<BigDecimal, Integer> simpleFunction = BigDecimal::intValue;
        List<TransactionEntity> singleTransaction = List.of(
                transactionEntity("A", "100", "2023-03-01T10:00:00.000000000Z"),
                transactionEntity("A", "50", "2023-02-28T10:00:00.000000000Z"),
                transactionEntity("A", "49", "2023-02-01T10:00:00.000000000Z")
        );

        List<CustomerRewardPoints> result = RewardFormatUtils.format(singleTransaction, simpleFunction);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.stream().filter(r -> r.getUserId().equals("A")).count());
        CustomerRewardPoints customerA = result.stream().filter(r -> r.getUserId().equals("A")).findFirst().get();
        Assertions.assertEquals(2, customerA.getMonthlyPoints().size());
        MonthlyPoints month3 = customerA.getMonthlyPoints().stream().filter(mp -> mp.getMonth() == 3).findFirst().get();
        Assertions.assertEquals(100, month3.getPoints());
        MonthlyPoints month2 = customerA.getMonthlyPoints().stream().filter(mp -> mp.getMonth() == 2).findFirst().get();
        Assertions.assertEquals(99, month2.getPoints());
        Assertions.assertEquals(199, customerA.getTotalPoints());

    }

    private TransactionEntity transactionEntity(String userId, String amount, String date) {
        return TransactionEntity.builder()
                .userId(userId)
                .amount(new BigDecimal(amount))
                .created(Instant.parse(date))
                .build();
    }

}
