CREATE TABLE accounts
(
    id      UUID PRIMARY KEY,
    balance NUMERIC(19, 2) NOT NULL,

    CONSTRAINT chk_accounts_balance_non_negative
        CHECK (balance >= 0)
);