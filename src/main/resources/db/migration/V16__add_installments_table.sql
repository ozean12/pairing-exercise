CREATE TABLE IF NOT EXISTS organisations_schema.installments
(
    id                      UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    grand_invoice_uid       UUID NOT NULL,
    order_uid               UUID NOT NULL,
    installment_amount      DECIMAL(12, 2) NOT NULL,
    status                  VARCHAR (100) NOT NULL DEFAULT 'CREATED',
    customer_uid            UUID NOT NULL,
    merchant_uid            UUID NOT NULL,
    date_created            TIMESTAMP NOT NULL,
    due_date                TIMESTAMP,
    last_update             TIMESTAMP
);
