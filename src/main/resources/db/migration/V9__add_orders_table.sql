CREATE TABLE IF NOT EXISTS organisations_schema.orders
(
   order_id            UUID PRIMARY KEY,
   order_timestamp     TIMESTAMP,
   organisation_id     UUID NOT NULL,
   customer_name       VARCHAR NOT NULL,
   customer_vat_number VARCHAR(20) NOT NULL,
   customer_address    VARCHAR NOT NULL,
   total_gross         DECIMAL(12, 2) NOT NULL,
   products            JSONB NOT NULL,
   FOREIGN KEY(organisation_id) REFERENCES organisations_schema.organisations(id)
);