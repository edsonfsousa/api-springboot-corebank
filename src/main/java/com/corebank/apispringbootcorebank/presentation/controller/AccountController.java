package com.corebank.apispringbootcorebank.presentation.controller;

import com.corebank.apispringbootcorebank.application.output.CreateAccountOutput;
import com.corebank.apispringbootcorebank.application.output.GetBalanceOutput;
import com.corebank.apispringbootcorebank.application.usecase.CreateAccountUseCase;
import com.corebank.apispringbootcorebank.application.usecase.GetBalanceUseCase;
import com.corebank.apispringbootcorebank.presentation.response.CreateAccountResponse;
import com.corebank.apispringbootcorebank.presentation.response.GetBalanceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetBalanceUseCase getBalanceUseCase;

    public AccountController(
            CreateAccountUseCase createAccountUseCase,
            GetBalanceUseCase getBalanceUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.getBalanceUseCase = getBalanceUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount() {
        CreateAccountOutput output = createAccountUseCase.execute();

        CreateAccountResponse response = new CreateAccountResponse(
                output.accountId(),
                output.balance()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<GetBalanceResponse> getBalance(
            @PathVariable UUID accountId
    ) {
        GetBalanceOutput output = getBalanceUseCase.execute(accountId);

        GetBalanceResponse response = new GetBalanceResponse(
                output.accountId(),
                output.balance()
        );

        return ResponseEntity.ok(response);
    }
}