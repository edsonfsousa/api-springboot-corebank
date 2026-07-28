package com.corebank.apispringbootcorebank.application.input;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionInput(
        UUID accountId,
        BigDecimal amount,
        String description
) {
}