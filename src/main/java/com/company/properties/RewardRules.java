package com.company.properties;

import com.company.model.RewardRule;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("")
@Getter
public class RewardRules {
    private final Map<String, RewardRule> rewardRules = new HashMap<>();

    public Map<String, RewardRule> getRewardRules() {
        return rewardRules;
    }
}
