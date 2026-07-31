package com.corebank.apispringbootcorebank.infrastructure.persistence.adapter;

import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;
import com.corebank.apispringbootcorebank.infrastructure.observability.BalanceMetrics;
import com.corebank.apispringbootcorebank.infrastructure.persistence.entity.AccountJpaEntity;
import com.corebank.apispringbootcorebank.infrastructure.persistence.mapper.AccountPersistenceMapper;
import com.corebank.apispringbootcorebank.infrastructure.persistence.repository.SpringDataAccountRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AccountPersistenceAdapter implements AccountGateway {

    private final SpringDataAccountRepository repository;
    private final AccountPersistenceMapper mapper;
    private final BalanceMetrics balanceMetrics;

    public AccountPersistenceAdapter(
            SpringDataAccountRepository repository,
            AccountPersistenceMapper mapper,
            BalanceMetrics balanceMetrics
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.balanceMetrics = balanceMetrics;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = mapper.toEntity(account);

        AccountJpaEntity savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(UUID accountId) {
        balanceMetrics.recordDatabaseQuery();

        return balanceMetrics.measureDatabase(
                () -> repository
                        .findById(accountId)
                        .map(mapper::toDomain)
        );
    }

    @Override
    public Optional<Account> findByIdForUpdate(UUID accountId) {
        return repository.findByIdForUpdate(accountId)
                .map(mapper::toDomain);
    }
}
