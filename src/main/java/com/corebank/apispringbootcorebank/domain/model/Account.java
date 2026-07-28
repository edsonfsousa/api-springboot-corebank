package com.corebank.apispringbootcorebank.domain.model;

import com.corebank.apispringbootcorebank.domain.exception.InsufficientBalanceException;
import com.corebank.apispringbootcorebank.domain.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public class Account {

    private static final int MONEY_SCALE = 2;

    private final UUID id;
    private BigDecimal balance;

    private Account(UUID id, BigDecimal balance) {
        this.id = Objects.requireNonNull(
                id,
                "Account id must not be null"
        );

        this.balance = normalizeBalance(balance);
    }

    public static Account create() {
        return new Account(
                UUID.randomUUID(),
                BigDecimal.ZERO
        );
    }

    public static Account restore(
            UUID id,
            BigDecimal balance
    ) {
        return new Account(id, balance);
    }

    public void applyTransaction(BigDecimal amount) {
        BigDecimal validAmount = normalizeAmount(amount);

        if (balance.compareTo(validAmount) < 0) {
            throw new InsufficientBalanceException(
                    balance,
                    validAmount
            );
        }

        balance = balance.subtract(validAmount);
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    private BigDecimal normalizeAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        return amount.setScale(
                MONEY_SCALE,
                RoundingMode.HALF_EVEN
        );
    }

    private BigDecimal normalizeBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException(
                    "Account balance must not be null"
            );
        }

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Account balance must not be negative"
            );
        }

        return balance.setScale(
                MONEY_SCALE,
                RoundingMode.HALF_EVEN
        );
    }
}