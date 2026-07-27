package com.corebank.apispringbootcorebank.domain.gateway;

import com.corebank.apispringbootcorebank.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountGateway {

    Account save(Account account);

    Optional<Account> findById(UUID accountId);
}