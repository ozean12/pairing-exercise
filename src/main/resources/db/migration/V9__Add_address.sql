CREATE TABLE IF NOT EXISTS organisations_schema.addresses
(
    id             UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    city_id        UUID REFERENCES organisations_schema.cities(id),
    street         VARCHAR(255) NOT NULL,
    house_number   VARCHAR(255) NOT NULL,
    postal_code    VARCHAR(255) NOT NULL,
    additional_info VARCHAR(255) NOT NULL
);

ALTER TABLE organisations_schema.organisations
ADD COLUMN address_id UUID REFERENCES organisations_schema.addresses(id);
