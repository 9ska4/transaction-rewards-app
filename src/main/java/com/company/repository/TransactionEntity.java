package com.company.repository;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity(name = "TRANSACTIONS")
@Table(indexes = {@Index(name = "uid_idx", columnList = "userId")})
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String userId;

    private BigDecimal amount;

    private Instant created;
}
