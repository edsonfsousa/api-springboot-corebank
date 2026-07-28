package com.corebank.apispringbootcorebank.application.usecase.implementation;

import com.corebank.apispringbootcorebank.application.input.CreateTransactionInput;
import com.corebank.apispringbootcorebank.application.output.CreateTransactionOutput;
import com.corebank.apispringbootcorebank.application.usecase.CreateTransactionUseCase;
import com.corebank.apispringbootcorebank.domain.exception.AccountNotFoundException;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.gateway.TransactionGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;
import com.corebank.apispringbootcorebank.domain.model.Transaction;

import java.util.Objects;

public class CreateTransactionService
        implements CreateTransactionUseCase {

    private final AccountGateway accountGateway;
    private final TransactionGateway transactionGateway;

    public CreateTransactionService(
            AccountGateway accountGateway,
            TransactionGateway transactionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.transactionGateway = Objects.requireNonNull(transactionGateway);
    }

    @Override
    public CreateTransactionOutput execute(
            CreateTransactionInput input
    ) {
        Objects.requireNonNull(
                input,
                "Create transaction input must not be null"
        );

        Account account = accountGateway
                .findByIdForUpdate(input.accountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(input.accountId())
                );

        account.applyTransaction(input.amount());

        Transaction transaction = Transaction.create(
                account.getId(),
                input.amount(),
                input.description()
        );

        Account savedAccount = accountGateway.save(account);

        Transaction savedTransaction =
                transactionGateway.save(transaction);

        return new CreateTransactionOutput(
                savedTransaction.getId(),
                savedTransaction.getAccountId(),
                savedTransaction.getAmount(),
                savedAccount.getBalance(),
                savedTransaction.getDescription(),
                savedTransaction.getCreatedAt()
        );
    }
}