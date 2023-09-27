
CREATE TABLE IF NOT EXISTS organisations_schema.addresses
(
    id                  UUID DEFAULT    uuid_generate_v4() PRIMARY KEY,
    street              VARCHAR(50)     NOT NULL,
    number              VARCHAR(20)     NOT NULL,
    postal_code         VARCHAR(20)     NOT NULL,
    city_id             UUID            NOT NULL references organisations_schema.cities(id)
);
