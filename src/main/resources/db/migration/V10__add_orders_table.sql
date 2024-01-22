CREATE TABLE IF NOT EXISTS orders_schema.orders
(
    id              UUID                     DEFAULT uuid_generate_v4() PRIMARY KEY,
    organisation_id UUID           NOT NULL,
    total_amount    DECIMAL(10, 2) NOT NULL,
    status          VARCHAR(20)    NOT NULL,
    no_of_items     INT            NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP WITH TIME ZONE
);
