package com.corebank.apispringbootcorebank.presentation.controller;

import com.corebank.apispringbootcorebank.application.output.CreateAccountOutput;
import com.corebank.apispringbootcorebank.application.output.GetBalanceOutput;
import com.corebank.apispringbootcorebank.application.usecase.CreateAccountUseCase;
import com.corebank.apispringbootcorebank.application.usecase.GetBalanceUseCase;
import com.corebank.apispringbootcorebank.domain.exception.AccountNotFoundException;
import com.corebank.apispringbootcorebank.presentation.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(GlobalExceptionHandler.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;

    @MockitoBean
    private GetBalanceUseCase getBalanceUseCase;

    @Test
    void shouldCreateAccountAndReturnCreated() throws Exception {
        UUID accountId = UUID.randomUUID();

        CreateAccountOutput output = new CreateAccountOutput(
                accountId,
                new BigDecimal("1.00")
        );

        when(createAccountUseCase.execute())
                .thenReturn(output);

        mockMvc.perform(post("/api/v1/accounts"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(accountId.toString()))
                .andExpect(jsonPath("$.balance").value(1.00));

        verify(createAccountUseCase).execute();
    }

    @Test
    void shouldReturnAccountBalance() throws Exception {
        UUID accountId = UUID.randomUUID();

        GetBalanceOutput output = new GetBalanceOutput(
                accountId,
                new BigDecimal("150.50")
        );

        when(getBalanceUseCase.execute(accountId))
                .thenReturn(output);

        mockMvc.perform(
                        get("/api/v1/accounts/{accountId}/balance", accountId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(accountId.toString()))
                .andExpect(jsonPath("$.balance").value(150.50));

        verify(getBalanceUseCase).execute(accountId);
    }

    @Test
    void shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
        UUID accountId = UUID.randomUUID();

        when(getBalanceUseCase.execute(accountId))
                .thenThrow(new AccountNotFoundException(accountId));

        mockMvc.perform(
                        get("/api/v1/accounts/{accountId}/balance", accountId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/accounts/" + accountId + "/balance"));

        verify(getBalanceUseCase).execute(accountId);
    }

    @Test
    void shouldReturnBadRequestWhenAccountIdIsInvalid() throws Exception {
        mockMvc.perform(
                        get("/api/v1/accounts/{accountId}/balance", "invalid-uuid")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Invalid request parameter: accountId"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/accounts/invalid-uuid/balance"));
    }
}