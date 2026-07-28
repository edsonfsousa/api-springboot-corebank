package com.corebank.apispringbootcorebank.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountJpaEntity {

    @Id
    private UUID id;

    @Column(
            name = "balance",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal balance;

    protected AccountJpaEntity() {
        // Construtor obrigatório para o JPA
    }

    public AccountJpaEntity(UUID id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}