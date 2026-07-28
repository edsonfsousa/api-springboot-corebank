package com.corebank.apispringbootcorebank.application.usecase.implementation;

import com.corebank.apispringbootcorebank.application.input.CreateTransactionInput;
import com.corebank.apispringbootcorebank.application.output.CreateTransactionOutput;
import com.corebank.apispringbootcorebank.application.usecase.CreateTransactionUseCase;
import com.corebank.apispringbootcorebank.domain.exception.AccountNotFoundException;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.gateway.TransactionGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;
import com.corebank.apispringbootcorebank.domain.model.Transaction;

public class CreateTransactionService
        implements CreateTransactionUseCase {

    private final AccountGateway accountGateway;
    private final TransactionGateway transactionGateway;

    public CreateTransactionService(
            AccountGateway accountGateway,
            TransactionGateway transactionGateway
    ) {
        this.accountGateway = accountGateway;
        this.transactionGateway = transactionGateway;
    }

    @Override
    public CreateTransactionOutput execute(
            CreateTransactionInput input
    ) {
        Account account = accountGateway
                .findByIdForUpdate(input.accountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(input.accountId())
                );

        account.applyTransaction(input.amount());

        Transaction transaction = Transaction.create(
                input.accountId(),
                input.amount(),
                input.description()
        );

        accountGateway.save(account);
        Transaction savedTransaction =
                transactionGateway.save(transaction);

        return new CreateTransactionOutput(
                savedTransaction.getId(),
                savedTransaction.getAccountId(),
                savedTransaction.getAmount(),
                account.getBalance(),
                savedTransaction.getDescription(),
                savedTransaction.getCreatedAt()
        );
    }
}