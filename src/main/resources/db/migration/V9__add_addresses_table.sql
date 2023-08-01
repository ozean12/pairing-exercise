CREATE TABLE IF NOT EXISTS organisations_schema.addresses
(
    id                  UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    city_id             UUID REFERENCES organisations_schema.cities(id),
    zip_code            VARCHAR(10) NOT NULL,
    street              VARCHAR(50) NOT NULL,
    street_number       VARCHAR(10) NOT NULL,
    apartment_number    VARCHAR(5)
);