package com.company.service;

import com.company.model.RewardRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class RewardRuleUtilsTest {

    @Test
    public void calculatePointsForSingleRule() {
        List<RewardRule> rules = List.of(
                RewardRule.builder().factor(1).from(50).build()
        );
        Function<BigDecimal, Integer> function = RewardRuleUtils.createRewardFunction(rules);
        Assertions.assertEquals(1150,  function.apply(new BigDecimal("1200.00")));
        Assertions.assertEquals(70,  function.apply(new BigDecimal("120.00")));
        Assertions.assertEquals(0,  function.apply(new BigDecimal("50.00")));
        Assertions.assertEquals(0,  function.apply(new BigDecimal("0.00")));
    }

    @Test
    public void calculatePointsForMultipleRules() {
        List<RewardRule> rules = List.of(
                RewardRule.builder().factor(1).from(50).build(),
                RewardRule.builder().factor(1).from(100).build()
        );
        Function<BigDecimal, Integer> function = RewardRuleUtils.createRewardFunction(rules);
        Assertions.assertEquals(2250,  function.apply(new BigDecimal("1200.00")));
        Assertions.assertEquals(90,  function.apply(new BigDecimal("120.00")));
        Assertions.assertEquals(0,  function.apply(new BigDecimal("50.00")));
        Assertions.assertEquals(0,  function.apply(new BigDecimal("0.00")));
    }
}
