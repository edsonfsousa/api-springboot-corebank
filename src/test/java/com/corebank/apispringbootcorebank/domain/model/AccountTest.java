package com.corebank.apispringbootcorebank.domain.model;

import com.corebank.apispringbootcorebank.domain.exception.InsufficientBalanceException;
import com.corebank.apispringbootcorebank.domain.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
    void shouldCreditAccount() {
        Account account = Account.create();

        account.credit(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), account.getBalance());
    }

    @Test
    void shouldDebitAccount() {
        Account account = Account.create();
        account.credit(new BigDecimal("100.00"));

        account.debit(new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), account.getBalance());
    }

    @Test
    void shouldAllowDebitUsingEntireBalance() {
        Account account = Account.create();
        account.credit(new BigDecimal("100.00"));

        account.debit(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("0.00"), account.getBalance());
    }

    @Test
    void shouldRejectDebitGreaterThanBalance() {
        Account account = Account.create();
        account.credit(new BigDecimal("100.00"));

        assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(new BigDecimal("100.01"))
        );

        assertEquals(new BigDecimal("100.00"), account.getBalance());
    }

    @Test
    void shouldRejectZeroCreditAmount() {
        Account account = Account.create();

        assertThrows(
                InvalidAmountException.class,
                () -> account.credit(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectNegativeDebitAmount() {
        Account account = Account.create();

        assertThrows(
                InvalidAmountException.class,
                () -> account.debit(new BigDecimal("-10.00"))
        );
    }

    @Test
    void shouldRejectNullAmount() {
        Account account = Account.create();

        assertThrows(
                InvalidAmountException.class,
                () -> account.credit(null)
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