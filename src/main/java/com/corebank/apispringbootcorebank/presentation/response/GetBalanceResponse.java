package com.corebank.apispringbootcorebank.presentation.response;

import java.math.BigDecimal;
import java.util.UUID;

public record GetBalanceResponse(
        UUID accountId,
        BigDecimal balance
) {
}