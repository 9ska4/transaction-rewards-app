package com.company.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class Transaction {

    private long id;
    private String userId;
    private BigDecimal amount;
    private Instant created;
}
