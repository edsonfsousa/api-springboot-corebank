package com.corebank.apispringbootcorebank.application.usecase.implementation;

import com.corebank.apispringbootcorebank.application.output.CreateAccountOutput;
import com.corebank.apispringbootcorebank.application.usecase.CreateAccountUseCase;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;

public class CreateAccountService implements CreateAccountUseCase {

    private final AccountGateway accountGateway;

    public CreateAccountService(AccountGateway accountGateway) {
        this.accountGateway = accountGateway;
    }

    @Override
    public CreateAccountOutput execute() {
        Account account = Account.create();

        Account savedAccount = accountGateway.save(account);

        return new CreateAccountOutput(
                savedAccount.getId(),
                savedAccount.getBalance()
        );
    }
}