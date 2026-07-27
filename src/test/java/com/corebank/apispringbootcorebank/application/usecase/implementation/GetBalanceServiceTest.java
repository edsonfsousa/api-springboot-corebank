package com.corebank.apispringbootcorebank.application.usecase.implementation;

import com.corebank.apispringbootcorebank.application.output.GetBalanceOutput;
import com.corebank.apispringbootcorebank.domain.exception.AccountNotFoundException;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetBalanceServiceTest {

    private final AccountGateway accountGateway = mock(AccountGateway.class);
    private final GetBalanceService service =
            new GetBalanceService(accountGateway);

    @Test
    void shouldReturnAccountBalance() {
        UUID accountId = UUID.randomUUID();

        Account account = Account.restore(
                accountId,
                new BigDecimal("250.00")
        );

        when(accountGateway.findById(accountId))
                .thenReturn(Optional.of(account));

        GetBalanceOutput output = service.execute(accountId);

        assertEquals(accountId, output.accountId());
        assertEquals(new BigDecimal("250.00"), output.balance());

        verify(accountGateway).findById(accountId);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {
        UUID accountId = UUID.randomUUID();

        when(accountGateway.findById(accountId))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> service.execute(accountId)
        );

        verify(accountGateway).findById(accountId);
    }
}