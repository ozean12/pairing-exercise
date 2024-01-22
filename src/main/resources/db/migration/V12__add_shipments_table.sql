CREATE TABLE IF NOT EXISTS shipments_schema.shipments
(
    id              UUID                     DEFAULT uuid_generate_v4() PRIMARY KEY,
    order_id        UUID           NOT NULL,
    shipment_amount DECIMAL(10, 2) NOT NULL,
    shipped_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);