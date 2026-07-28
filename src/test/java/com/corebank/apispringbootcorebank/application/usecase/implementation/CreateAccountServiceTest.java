package com.corebank.apispringbootcorebank.application.usecase.implementation;

import com.corebank.apispringbootcorebank.application.output.CreateAccountOutput;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAccountServiceTest {

    private final AccountGateway accountGateway = mock(AccountGateway.class);
    private final CreateAccountService service =
            new CreateAccountService(accountGateway);

    @Test
    void shouldCreateAccountWithZeroBalance() {
        when(accountGateway.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateAccountOutput output = service.execute();

        assertNotNull(output.accountId());
        assertEquals("0.00", output.balance().toPlainString());

        verify(accountGateway).save(any(Account.class));
    }
}