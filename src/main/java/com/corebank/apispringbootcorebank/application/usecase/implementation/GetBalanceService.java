package com.corebank.apispringbootcorebank.application.usecase.implementation;

import com.corebank.apispringbootcorebank.application.output.GetBalanceOutput;
import com.corebank.apispringbootcorebank.application.usecase.GetBalanceUseCase;
import com.corebank.apispringbootcorebank.domain.exception.AccountNotFoundException;
import com.corebank.apispringbootcorebank.domain.gateway.AccountBalanceCacheGateway;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class GetBalanceService implements GetBalanceUseCase {

    private final AccountGateway accountGateway;
    private final AccountBalanceCacheGateway accountBalanceCacheGateway;

    public GetBalanceService(
            AccountGateway accountGateway,
            AccountBalanceCacheGateway accountBalanceCacheGateway
    ) {
        this.accountGateway = accountGateway;
        this.accountBalanceCacheGateway = accountBalanceCacheGateway;
    }

    @Override
    public GetBalanceOutput execute(UUID accountId) {
        Optional<BigDecimal> cachedBalance =
                accountBalanceCacheGateway.findBalance(accountId);

        if (cachedBalance.isPresent()) {
            return new GetBalanceOutput(
                    accountId,
                    cachedBalance.get()
            );
        }

        Account account = accountGateway
                .findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(accountId)
                );

        BigDecimal balance = account.getBalance();

        accountBalanceCacheGateway.saveBalance(
                accountId,
                balance
        );

        return new GetBalanceOutput(
                accountId,
                balance
        );
    }
}