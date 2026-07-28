package com.corebank.apispringbootcorebank.presentation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAccountResponse(
        UUID accountId,
        BigDecimal balance
) {
}