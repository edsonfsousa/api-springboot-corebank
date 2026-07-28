CREATE TABLE transactions
(
    id          UUID PRIMARY KEY,
    account_id  UUID                     NOT NULL,
    amount      NUMERIC(19, 2)           NOT NULL,
    description VARCHAR(255)             NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_transactions_account
        FOREIGN KEY (account_id)
            REFERENCES accounts (id)
);

CREATE INDEX idx_transactions_account_id
    ON transactions (account_id);

CREATE INDEX idx_transactions_created_at
    ON transactions (created_at);