CREATE TABLE IF NOT EXISTS organisations_schema.cities
(
    id           UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    country_code CHAR(2)      NOT NULL,
    name         VARCHAR(100) NOT NULL
);
