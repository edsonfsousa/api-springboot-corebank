package com.corebank.apispringbootcorebank.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

    private UUID id;
    private BigDecimal balance;

    public Account(UUID id, BigDecimal balance) {
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