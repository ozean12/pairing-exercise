BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS organisations_schema.addresses
(
    id                   UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    street_name          VARCHAR(50),
    house_number         VARCHAR(20),
    city                 VARCHAR(50),
    additional_address   VARCHAR(256),
    pin_code             VARCHAR(20),
    country              VARCHAR(100)
);
ALTER TABLE organisations_schema.organisations ADD COLUMN addresses_id UUID REFERENCES organisations_schema.addresses(id);
COMMIT;