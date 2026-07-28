package com.corebank.apispringbootcorebank.presentation.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateTransactionResponse(
        UUID transactionId,
        UUID accountId,
        BigDecimal amount,
        BigDecimal balance,
        String description,
        Instant createdAt
) {
}