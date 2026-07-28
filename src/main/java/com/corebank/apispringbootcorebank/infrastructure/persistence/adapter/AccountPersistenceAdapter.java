package com.corebank.apispringbootcorebank.infrastructure.persistence.adapter;

import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;
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

    public AccountPersistenceAdapter(
            SpringDataAccountRepository repository,
            AccountPersistenceMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = mapper.toEntity(account);

        AccountJpaEntity savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(UUID accountId) {
        return repository.findById(accountId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Account> findByIdForUpdate(UUID accountId) {
        return repository.findByIdForUpdate(accountId)
                .map(mapper::toDomain);
    }
}