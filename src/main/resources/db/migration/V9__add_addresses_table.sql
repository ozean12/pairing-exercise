CREATE TABLE IF NOT EXISTS organisations_schema.addresses
(
    id                UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    city              VARCHAR(100),
    street_and_number VARCHAR(100),
    postal_code       VARCHAR(20)
);

ALTER TABLE organisations_schema.organisations ADD COLUMN address_id VARCHAR(36);

