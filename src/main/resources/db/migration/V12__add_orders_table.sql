CREATE TABLE IF NOT EXISTS organisations_schema.customer_orders
(
    id              UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    order_amount    DECIMAL(12, 2) NOT NULL,
    customer_uid    UUID NOT NULL
);
