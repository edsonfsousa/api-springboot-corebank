package com.corebank.apispringbootcorebank.infrastructure.transaction;

import com.corebank.apispringbootcorebank.application.input.CreateTransactionInput;
import com.corebank.apispringbootcorebank.application.output.CreateTransactionOutput;
import com.corebank.apispringbootcorebank.application.usecase.CreateTransactionUseCase;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

public class CreateTransactionTransactionalDecorator
        implements CreateTransactionUseCase {

    private final CreateTransactionUseCase delegate;
    private final TransactionTemplate transactionTemplate;

    public CreateTransactionTransactionalDecorator(
            CreateTransactionUseCase delegate,
            TransactionTemplate transactionTemplate
    ) {
        this.delegate = Objects.requireNonNull(delegate);
        this.transactionTemplate =
                Objects.requireNonNull(transactionTemplate);
    }

    @Override
    public CreateTransactionOutput execute(
            CreateTransactionInput input
    ) {
        return transactionTemplate.execute(
                status -> delegate.execute(input)
        );
    }
}