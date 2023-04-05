package com.company.service;

import com.company.model.RewardRule;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Function;

@UtilityClass
public class RewardRuleUtils {

    public Function<BigDecimal, Integer> createRewardFunction(Collection<RewardRule> rewardRules) {
        return (bigDecimal) -> {
            int v = (int) Math.floor(bigDecimal.doubleValue());
            return rewardRules.stream()
                    .filter(rule -> v > rule.getFrom())
                    .map(rule -> (rule.getFactor() * (v - rule.getFrom())))
                    .mapToInt(Integer::intValue)
                    .sum();
        };
    }
}
