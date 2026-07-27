package com.corebank.apispringbootcorebank.domain.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends DomainException {

    public InsufficientBalanceException(
            BigDecimal currentBalance,
            BigDecimal requestedAmount
    ) {
        super(
                "Insufficient balance. Current balance: %s, requested amount: %s"
                        .formatted(currentBalance, requestedAmount)
        );
    }
}