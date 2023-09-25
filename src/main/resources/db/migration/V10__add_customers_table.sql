CREATE TABLE IF NOT EXISTS organisations_schema.customers
(
    id                  UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name                VARCHAR(256) NOT NULL,
    address_details     VARCHAR(256) NOT NULL
);
