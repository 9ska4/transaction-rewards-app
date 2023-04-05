package com.company.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerRewardPoints {

    private String userId;
    private List<MonthlyPoints> monthlyPoints;
    private Integer totalPoints;
}
