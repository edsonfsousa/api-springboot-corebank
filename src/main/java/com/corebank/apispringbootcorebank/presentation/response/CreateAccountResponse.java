package com.corebank.apispringbootcorebank.presentation.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAccountResponse(
        UUID accountId,
        BigDecimal balance
) {
}