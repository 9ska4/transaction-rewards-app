package com.company.service;


import com.company.model.CustomerRewardPoints;
import com.company.model.RewardRule;
import com.company.properties.RewardRules;
import com.company.repository.TransactionEntity;
import com.company.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class RewardServiceTest {

    @Autowired
    private RewardService rewardService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private RewardRules rewardRules;

    @Test
    public void checkGetAllTransaction() {
        List<TransactionEntity> singleTransaction = List.of(
                transactionEntity("A", "11", "2023-03-20T10:00:00.000000000Z")
        );
        Map<String, RewardRule> singleRewardRule = Map.of(
                "reward1", RewardRule.builder().from(0).factor(3).build()
        );

        Mockito.when(transactionRepository.findAllByCreatedAfterAndAmountGreaterThan(any(), any()))
                .thenReturn(singleTransaction);
        Mockito.when(rewardRules.getRewardRules())
                .thenReturn(singleRewardRule);

        List<CustomerRewardPoints> result = rewardService.getAll();

        Assertions.assertEquals(1, result.size(), "contains single customer");
        Assertions.assertEquals(33, result.get(0).getTotalPoints(), "contains 3x11 points");
        Assertions.assertEquals(1, result.get(0).getMonthlyPoints().size(), "contains points only for one month");
    }

    private TransactionEntity transactionEntity(String userId, String amount, String date) {
        return TransactionEntity.builder()
                .userId(userId)
                .amount(new BigDecimal(amount))
                .created(Instant.parse(date))
                .build();
    }

}
