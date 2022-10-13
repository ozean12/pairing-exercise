CREATE TABLE IF NOT EXISTS organisations_schema.countries
(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    country_code VARCHAR(2)   NOT NULL
);
