package com.corebank.apispringbootcorebank.domain.model;

import com.corebank.apispringbootcorebank.domain.exception.InsufficientBalanceException;
import com.corebank.apispringbootcorebank.domain.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Test
    void shouldCreateAccountWithZeroBalance() {
        Account account = Account.create();

        assertNotNull(account.getId());
        assertEquals(new BigDecimal("0.00"), account.getBalance());
    }

    @Test
    void shouldRestoreExistingAccount() {
        UUID accountId = UUID.randomUUID();

        Account account = Account.restore(
                accountId,
                new BigDecimal("150.00")
        );

        assertEquals(accountId, account.getId());
        assertEquals(new BigDecimal("150.00"), account.getBalance());
    }

    @Test
    void shouldApplyTransaction() {
        Account account = Account.restore(
                UUID.randomUUID(),
                new BigDecimal("100.00")
        );

        account.applyTransaction(new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), account.getBalance());
    }

    @Test
    void shouldAllowTransactionUsingEntireBalance() {
        Account account = Account.restore(
                UUID.randomUUID(),
                new BigDecimal("100.00")
        );

        account.applyTransaction(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("0.00"), account.getBalance());
    }

    @Test
    void shouldRejectTransactionGreaterThanBalance() {
        Account account = Account.restore(
                UUID.randomUUID(),
                new BigDecimal("100.00")
        );

        assertThrows(
                InsufficientBalanceException.class,
                () -> account.applyTransaction(
                        new BigDecimal("100.01")
                )
        );

        assertEquals(new BigDecimal("100.00"), account.getBalance());
    }

    @Test
    void shouldRejectTransactionWithZeroAmount() {
        Account account = Account.restore(
                UUID.randomUUID(),
                new BigDecimal("100.00")
        );

        assertThrows(
                InvalidAmountException.class,
                () -> account.applyTransaction(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectTransactionWithNegativeAmount() {
        Account account = Account.restore(
                UUID.randomUUID(),
                new BigDecimal("100.00")
        );

        assertThrows(
                InvalidAmountException.class,
                () -> account.applyTransaction(
                        new BigDecimal("-10.00")
                )
        );
    }

    @Test
    void shouldRejectTransactionWithNullAmount() {
        Account account = Account.restore(
                UUID.randomUUID(),
                new BigDecimal("100.00")
        );

        assertThrows(
                InvalidAmountException.class,
                () -> account.applyTransaction(null)
        );
    }

    @Test
    void shouldRejectRestoringAccountWithNegativeBalance() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Account.restore(
                        UUID.randomUUID(),
                        new BigDecimal("-0.01")
                )
        );
    }
}