package com.corebank.apispringbootcorebank.application.usecase;

import java.math.BigDecimal;
import java.util.UUID;

public interface GetBalanceUseCase {

    BigDecimal execute(UUID accountId);

}