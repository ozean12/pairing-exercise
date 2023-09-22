CREATE TABLE IF NOT EXISTS organisations_schema.address
(
    id            UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    address1      VARCHAR(120) NOT NULL,
    address2      VARCHAR(120),
    country_code  CHAR(2) NOT NULL,
    city          CHAR(2) NOT NULL,
    state         CHAR(120),
    postal_code   VARCHAR(16) NOT NULL
);