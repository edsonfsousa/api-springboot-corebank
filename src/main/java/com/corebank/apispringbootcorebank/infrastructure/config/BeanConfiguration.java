package com.corebank.apispringbootcorebank.infrastructure.config;

import com.corebank.apispringbootcorebank.application.usecase.CreateAccountUseCase;
import com.corebank.apispringbootcorebank.application.usecase.CreateTransactionUseCase;
import com.corebank.apispringbootcorebank.application.usecase.GetBalanceUseCase;
import com.corebank.apispringbootcorebank.application.usecase.implementation.CreateAccountService;
import com.corebank.apispringbootcorebank.application.usecase.implementation.CreateTransactionService;
import com.corebank.apispringbootcorebank.application.usecase.implementation.GetBalanceService;
import com.corebank.apispringbootcorebank.domain.gateway.AccountBalanceCacheGateway;
import com.corebank.apispringbootcorebank.domain.gateway.AccountGateway;
import com.corebank.apispringbootcorebank.domain.gateway.TransactionGateway;
import com.corebank.apispringbootcorebank.infrastructure.persistence.adapter.TransactionPersistenceAdapter;
import com.corebank.apispringbootcorebank.infrastructure.persistence.repository.TransactionJpaRepository;
import com.corebank.apispringbootcorebank.infrastructure.transaction.CreateTransactionTransactionalDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

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
            AccountGateway accountGateway,
            AccountBalanceCacheGateway accountBalanceCacheGateway
    ) {
        return new GetBalanceService(
                accountGateway,
                accountBalanceCacheGateway
        );
    }

    @Bean
    public TransactionGateway transactionGateway(
            TransactionJpaRepository transactionJpaRepository
    ) {
        return new TransactionPersistenceAdapter(
                transactionJpaRepository
        );
    }

    @Bean
    public CreateTransactionUseCase createTransactionUseCase(
            AccountGateway accountGateway,
            TransactionGateway transactionGateway
    ) {
        return new CreateTransactionService(
                accountGateway,
                transactionGateway
        );
    }

    @Bean
    public TransactionTemplate transactionTemplate(
            PlatformTransactionManager transactionManager
    ) {
        return new TransactionTemplate(transactionManager);
    }

    @Bean
    public CreateTransactionUseCase createTransactionUseCase(
            AccountGateway accountGateway,
            TransactionGateway transactionGateway,
            TransactionTemplate transactionTemplate
    ) {
        CreateTransactionUseCase service =
                new CreateTransactionService(
                        accountGateway,
                        transactionGateway
                );

        return new CreateTransactionTransactionalDecorator(
                service,
                transactionTemplate
        );
    }
}