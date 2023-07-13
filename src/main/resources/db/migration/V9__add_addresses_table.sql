CREATE TABLE IF NOT EXISTS organisations_schema.addresses
(
    id             UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    address_line_1 VARCHAR(255) NOT NULL,
    address_line_2 VARCHAR(255),
    zip_code       VARCHAR(32)  NOT NULL,
    city           VARCHAR(100) NOT NULL,
    country_code   CHAR(2)      NOT NULL
);

ALTER TABLE IF EXISTS organisations_schema.organisations
    ADD COLUMN address_id UUID REFERENCES organisations_schema.addresses(id) ON DELETE CASCADE;
