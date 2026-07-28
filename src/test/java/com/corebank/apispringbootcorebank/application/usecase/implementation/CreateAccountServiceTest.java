package com.corebank.apispringbootcorebank.application.usecase.implementation;

import com.corebank.apispringbootcorebank.application.output.CreateAccountOutput;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.model.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateAccountServiceTest {

    private final AccountGateway accountGateway = mock(AccountGateway.class);
    private final CreateAccountService service =
            new CreateAccountService(accountGateway);

    @Test
    void shouldCreateAccountWithInitialBalanceOfOneReal() {
        when(accountGateway.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateAccountOutput output = service.execute();

        assertNotNull(output.accountId());
        assertEquals(new BigDecimal("1.00"), output.balance());

        verify(accountGateway).save(argThat(account ->
                account.getId() != null
                        && new BigDecimal("1.00").compareTo(account.getBalance()) == 0
        ));
    }
}