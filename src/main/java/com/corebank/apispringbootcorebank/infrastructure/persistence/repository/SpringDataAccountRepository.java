package com.corebank.apispringbootcorebank.infrastructure.persistence.repository;

import com.corebank.apispringbootcorebank.infrastructure.persistence.entity.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAccountRepository
        extends JpaRepository<AccountJpaEntity, UUID> {
}