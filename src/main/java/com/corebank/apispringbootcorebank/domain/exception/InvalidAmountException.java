package com.corebank.apispringbootcorebank.domain.exception;

public class InvalidAmountException extends DomainException {

    public InvalidAmountException() {
        super("Amount must be greater than zero");
    }
}