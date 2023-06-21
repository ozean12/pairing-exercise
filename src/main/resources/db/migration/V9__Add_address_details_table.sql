
CREATE TABLE IF NOT EXISTS organisations_schema.address_details
(
    id             UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    city_id        VARCHAR(36) NOT NULL,
    country_code   CHAR(2)     NOT NULL,
    zip_code       VARCHAR(20) NOT NULL,
    address_string VARCHAR(256) NOT NULL
);
