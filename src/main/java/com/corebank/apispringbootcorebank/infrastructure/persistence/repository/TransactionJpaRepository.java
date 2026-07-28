package com.corebank.apispringbootcorebank.infrastructure.persistence.repository;

import com.corebank.apispringbootcorebank.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionJpaRepository
        extends JpaRepository<TransactionEntity, UUID> {
}
