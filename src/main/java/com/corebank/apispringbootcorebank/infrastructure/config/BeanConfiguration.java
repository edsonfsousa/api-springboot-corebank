package com.corebank.apispringbootcorebank.infrastructure.config;

import com.corebank.apispringbootcorebank.application.usecase.CreateAccountUseCase;
import com.corebank.apispringbootcorebank.application.usecase.GetBalanceUseCase;
import com.corebank.apispringbootcorebank.application.usecase.implementation.CreateAccountService;
import com.corebank.apispringbootcorebank.application.usecase.implementation.GetBalanceService;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreateAccountUseCase createAccountUseCase(
            AccountGateway accountGateway
    ) {
        return new CreateAccountService(accountGateway);
    }

    @Bean
    public GetBalanceUseCase getBalanceUseCase(
            AccountGateway accountGateway
    ) {
        return new GetBalanceService(accountGateway);
    }
}