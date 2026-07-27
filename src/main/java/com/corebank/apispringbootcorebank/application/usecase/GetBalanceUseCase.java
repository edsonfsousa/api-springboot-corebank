package com.corebank.apispringbootcorebank.application.usecase;

import com.corebank.apispringbootcorebank.application.output.GetBalanceOutput;

import java.util.UUID;

public interface GetBalanceUseCase {

    GetBalanceOutput execute(UUID accountId);
}