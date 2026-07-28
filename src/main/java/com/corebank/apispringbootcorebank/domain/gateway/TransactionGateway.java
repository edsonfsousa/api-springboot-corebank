package com.corebank.apispringbootcorebank.domain.gateway;

import com.corebank.apispringbootcorebank.domain.model.Transaction;

public interface TransactionGateway {

    Transaction save(Transaction transaction);
}