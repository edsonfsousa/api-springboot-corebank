package com.corebank.apispringbootcorebank.application.output;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateTransactionOutput(
        UUID transactionId,
        UUID accountId,
        BigDecimal amount,
        BigDecimal balance,
        String description,
        Instant createdAt
) {
}