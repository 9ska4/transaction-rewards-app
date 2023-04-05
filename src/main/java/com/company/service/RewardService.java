package com.company.service;

import com.company.model.*;
import com.company.properties.RewardRules;
import com.company.repository.TransactionEntity;
import com.company.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class RewardService {

    private final Clock clock;

    private final TransactionRepository transactionRepository;

    private final RewardRules rewardRules;

    public List<CustomerRewardPoints> getAll() {
        List<TransactionEntity> allTransactions = transactionRepository.findAllByCreatedAfterAndAmountGreaterThan(getDefaultFromDate(), BigDecimal.valueOf(50));

        Function<BigDecimal, Integer> rewardFunction = RewardRuleUtils.createRewardFunction(rewardRules.getRewardRules().values());

        List<CustomerRewardPoints> customerRewardPoints = RewardFormatUtils.format(allTransactions, rewardFunction);

        return customerRewardPoints;
    }

    public Instant getDefaultFromDate() {
        Instant instant = Instant.now(clock);
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        int year = zdt.getYear();
        int month = zdt.getMonthValue() - 2;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        return firstDayOfMonth.atStartOfDay(ZoneId.of("UTC")).toInstant();
    }

}
