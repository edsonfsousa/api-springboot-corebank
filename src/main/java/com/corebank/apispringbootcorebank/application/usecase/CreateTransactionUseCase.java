package com.corebank.apispringbootcorebank.application.usecase;

import com.corebank.apispringbootcorebank.application.input.CreateTransactionInput;
import com.corebank.apispringbootcorebank.application.output.CreateTransactionOutput;

public interface CreateTransactionUseCase {

    CreateTransactionOutput execute(CreateTransactionInput input);
}