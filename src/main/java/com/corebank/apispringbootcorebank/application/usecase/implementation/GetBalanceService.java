package com.corebank.apispringbootcorebank.application.usecase.implementation;

import com.corebank.apispringbootcorebank.application.output.GetBalanceOutput;
import com.corebank.apispringbootcorebank.application.usecase.GetBalanceUseCase;
import com.corebank.apispringbootcorebank.domain.exception.AccountNotFoundException;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;

import java.util.UUID;

public class GetBalanceService implements GetBalanceUseCase {

    private final AccountGateway accountGateway;

    public GetBalanceService(AccountGateway accountGateway) {
        this.accountGateway = accountGateway;
    }

    @Override
    public GetBalanceOutput execute(UUID accountId) {
        Account account = accountGateway.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        return new GetBalanceOutput(
                account.getId(),
                account.getBalance()
        );
    }
}