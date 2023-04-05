package com.company.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class CreateTransaction {

    @Pattern(regexp="^[a-zA-Z0-9]+$")
    private String userId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;

    private Instant created;
}
