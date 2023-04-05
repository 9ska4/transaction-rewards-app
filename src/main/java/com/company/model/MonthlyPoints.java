package com.company.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthlyPoints {

    private Integer month;
    private Integer points;
}
