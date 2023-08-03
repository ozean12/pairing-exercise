CREATE TABLE IF NOT EXISTS organisations_schema.address
(
    id           UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    country_code TEXT NOT NULL,
    city_name    TEXT NOT NULL,
    zip_code     TEXT NOT NULL,
    street       TEXT NOT NULL,
    house_number TEXT NOT NULL,
    comment      TEXT NOT NULL
);

ALTER TABLE organisations_schema.organisations
    ADD COLUMN IF NOT EXISTS main_address_id UUID
        CONSTRAINT fk_organisations_address_main REFERENCES organisations_schema.address (id);

