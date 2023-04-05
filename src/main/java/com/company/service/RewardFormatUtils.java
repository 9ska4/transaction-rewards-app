package com.company.service;

import com.company.model.CustomerRewardPoints;
import com.company.model.MonthlyPoints;
import com.company.repository.TransactionEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class RewardFormatUtils {

    public List<CustomerRewardPoints> format(List<TransactionEntity> transactions, Function<BigDecimal, Integer> rewardFunction) {
        Map<String, Map<Integer, Integer>> stringMapMap = doGrouping(transactions, rewardFunction);
        List<CustomerRewardPoints> customerRewardPoints = doFormatting(stringMapMap);
        return customerRewardPoints;
    }

    private Map<String, Map<Integer, Integer>> doGrouping(List<TransactionEntity> transactions, Function<BigDecimal, Integer> rewardFunction) {
        return transactions.stream()
                .collect(Collectors.groupingBy(TransactionEntity::getUserId,
                        Collectors.groupingBy(entity -> extractMonth(entity.getCreated()),
                                Collectors.mapping(entity -> rewardFunction.apply(entity.getAmount()),
                                        Collectors.summingInt(Integer::intValue)))));
    }

    private int extractMonth(Instant instant) {
        return ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getMonthValue();
    }

    private List<CustomerRewardPoints> doFormatting(Map<String, Map<Integer, Integer>> usersAndMonthsAndPoints) {
        List<CustomerRewardPoints> customerRewardPointsList = new ArrayList<>();

        for (Map.Entry<String, Map<Integer, Integer>> entry : usersAndMonthsAndPoints.entrySet()) {
            String userId = entry.getKey();
            Map<Integer, Integer> monthsAndPoints = entry.getValue();

            Integer totalUserPoints = 0;
            List<MonthlyPoints> monthlyPointsList = new ArrayList<>();

            for (Map.Entry<Integer, Integer> e : monthsAndPoints.entrySet()) {
                Integer month = e.getKey();
                Integer points = e.getValue();
                monthlyPointsList.add(MonthlyPoints.builder()
                        .month(month)
                        .points(points)
                        .build());
                totalUserPoints += points;
            }

            customerRewardPointsList.add(CustomerRewardPoints.builder()
                    .userId(userId)
                    .monthlyPoints(monthlyPointsList)
                    .totalPoints(totalUserPoints)
                    .build());
        }

        return customerRewardPointsList;
    }


}
