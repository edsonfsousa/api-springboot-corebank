package com.corebank.apispringbootcorebank.infrastructure.persistence.adapter;

import com.corebank.apispringbootcorebank.domain.model.Account;
import com.corebank.apispringbootcorebank.infrastructure.persistence.repository.SpringDataAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@Transactional
class AccountPersistenceAdapterIntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer("postgres:17-alpine")
                    .withDatabaseName("corebank_test")
                    .withUsername("corebank")
                    .withPassword("corebank");

    @Autowired
    private AccountPersistenceAdapter accountPersistenceAdapter;

    @Autowired
    private SpringDataAccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindAccount() {
        Account account = Account.create();

        Account savedAccount =
                accountPersistenceAdapter.save(account);

        var result =
                accountPersistenceAdapter.findById(account.getId());

        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getId())
                .isEqualTo(account.getId());

        assertThat(result).isPresent();

        Account restoredAccount = result.orElseThrow();

        assertThat(restoredAccount.getId())
                .isEqualTo(account.getId());

        assertThat(restoredAccount.getBalance())
                .isEqualByComparingTo("0.00");
    }

    @Test
    void shouldSaveAndRestoreAccountBalance() {
        Account account = Account.create();

        account.credit(new BigDecimal("150.75"));

        accountPersistenceAdapter.save(account);

        var result =
                accountPersistenceAdapter.findById(account.getId());

        assertThat(result).isPresent();

        Account restoredAccount = result.orElseThrow();

        assertThat(restoredAccount.getId())
                .isEqualTo(account.getId());

        assertThat(restoredAccount.getBalance())
                .isEqualByComparingTo("150.75");
    }

    @Test
    void shouldReturnEmptyWhenAccountDoesNotExist() {
        UUID nonexistentAccountId = UUID.randomUUID();

        var result =
                accountPersistenceAdapter.findById(nonexistentAccountId);

        assertThat(result).isEmpty();
    }
}