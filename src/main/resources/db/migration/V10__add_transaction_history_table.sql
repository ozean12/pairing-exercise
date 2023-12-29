CREATE TABLE organisations_schema.transaction_history
(
    id                        uuid             NOT NULL,
    payment_order_id          uuid             NOT NULL,
    amount_before_transaction double precision NOT NULL,
    deducted_amount           double precision NOT NULL,
    remaining_amount          double precision NOT NULL,
    created_at                timestamptz      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
