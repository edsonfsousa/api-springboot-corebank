package com.corebank.apispringbootcorebank.domain.exception;

import java.util.UUID;

public class AccountNotFoundException extends DomainException {

    public AccountNotFoundException(UUID accountId) {
        super("Account not found: " + accountId);
    }
}