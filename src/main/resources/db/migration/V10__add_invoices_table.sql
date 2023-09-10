CREATE TABLE IF NOT EXISTS organisations_schema.invoices
(
   id                UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
   order_id          UUID NOT NULL,
   invoice_timestamp TIMESTAMP NOT NULL,
   is_paid           BOOLEAN DEFAULT false,
   FOREIGN KEY(order_id) REFERENCES organisations_schema.orders(order_id)
);