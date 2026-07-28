package com.corebank.apispringbootcorebank.infrastructure.persistence.mapper;

import com.corebank.apispringbootcorebank.domain.model.Account;
import com.corebank.apispringbootcorebank.infrastructure.persistence.entity.AccountJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceMapper {

    public AccountJpaEntity toEntity(Account account) {
        return new AccountJpaEntity(
                account.getId(),
                account.getBalance()
        );
    }

    public Account toDomain(AccountJpaEntity entity) {
        return Account.restore(
                entity.getId(),
                entity.getBalance()
        );
    }
}