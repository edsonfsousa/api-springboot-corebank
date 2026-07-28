package com.corebank.apispringbootcorebank.infrastructure.persistence.adapter;

import com.corebank.apispringbootcorebank.domain.gateway.TransactionGateway;
import com.corebank.apispringbootcorebank.domain.model.Transaction;
import com.corebank.apispringbootcorebank.infrastructure.persistence.entity.TransactionEntity;
import com.corebank.apispringbootcorebank.infrastructure.persistence.repository.TransactionJpaRepository;

import java.util.Objects;

public class TransactionPersistenceAdapter implements TransactionGateway {

    private final TransactionJpaRepository repository;

    public TransactionPersistenceAdapter(TransactionJpaRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Transaction save(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction must not be null");

        TransactionEntity entity = new TransactionEntity(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );

        TransactionEntity saved = repository.save(entity);

        return Transaction.restore(
                saved.getId(),
                saved.getAccountId(),
                saved.getAmount(),
                saved.getDescription(),
                saved.getCreatedAt()
        );
    }
}