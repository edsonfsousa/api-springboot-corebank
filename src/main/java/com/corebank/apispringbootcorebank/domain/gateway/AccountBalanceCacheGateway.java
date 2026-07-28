package com.corebank.apispringbootcorebank.domain.gateway;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface AccountBalanceCacheGateway {

    Optional<BigDecimal> findBalance(UUID accountId);

    void saveBalance(
            UUID accountId,
            BigDecimal balance
    );

    void evict(UUID accountId);
}