package com.company.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class UpdateTransaction {

    @NotNull
    private Long id;

    @NotBlank
    private String userId;

    @NotNull
    private BigDecimal amount;

    private Instant created;
}
