package com.corebank.apispringbootcorebank.domain.model;

import com.corebank.apispringbootcorebank.domain.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private static final int MONEY_SCALE = 2;
    private static final int DESCRIPTION_MAX_LENGTH = 255;

    private final UUID id;
    private final UUID accountId;
    private final BigDecimal amount;
    private final String description;
    private final Instant createdAt;

    private Transaction(UUID id, UUID accountId, BigDecimal amount, String description, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "Transaction id must not be null");
        this.accountId = Objects.requireNonNull(accountId, "Account id must not be null");
        this.amount = validateAmount(amount);
        this.description = validateDescription(description);
        this.createdAt = Objects.requireNonNull(createdAt, "Transaction creation date must not be null");
    }

    public static Transaction create(UUID accountId, BigDecimal amount, String description) {
        return new Transaction(UUID.randomUUID(), accountId, amount, description, Instant.now());
    }

    public static Transaction restore(UUID id, UUID accountId, BigDecimal amount, String description, Instant createdAt) {
        return new Transaction(id, accountId, amount, description, createdAt);
    }

    public UUID getId() { return id; }
    public UUID getAccountId() { return accountId; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }

    private BigDecimal validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
        return amount.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);
    }

    private String validateDescription(String description) {
        String normalized = Objects.requireNonNull(
                description,
                "Transaction description must not be null"
        ).trim();

        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Transaction description must not be blank");
        }
        if (normalized.length() > DESCRIPTION_MAX_LENGTH) {
            throw new IllegalArgumentException("Transaction description must not exceed 255 characters");
        }
        return normalized;
    }
}
