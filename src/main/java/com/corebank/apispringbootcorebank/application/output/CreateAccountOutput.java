package com.corebank.apispringbootcorebank.application.output;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAccountOutput(
        UUID accountId,
        BigDecimal balance
) {
}