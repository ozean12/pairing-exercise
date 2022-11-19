CREATE TABLE IF NOT EXISTS organisations_schema.address_details
(
    id           UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    country_code CHAR(2) NOT NULL,
    state        VARCHAR(64),
    city         VARCHAR(100) NOT NULL,
    zip_code     VARCHAR(16),
    street       VARCHAR(196)
);
