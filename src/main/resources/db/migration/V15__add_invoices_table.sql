CREATE TABLE IF NOT EXISTS organisations_schema.invoices
(
    id                      UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    order_uid               UUID NOT NULL,
    order_amount            DECIMAL(12, 2) NOT NULL,
    overall_status          VARCHAR (100) NOT NULL DEFAULT 'CREATED',
    customer_uid            UUID NOT NULL,
    merchant_uid            UUID NOT NULL,
    date_created            TIMESTAMP NOT NULL,
    last_update             TIMESTAMP
);
