CREATE TABLE IF NOT EXISTS organisations_schema.addresses
(
    id                      UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    organisation_id         UUID NOT NULL,
    city_id                 UUID NOT NULL,
    pin_code                VARCHAR(10) NOT NULL,
    street_name             VARCHAR(100) NOT NULL,
    plot_number             VARCHAR(5) NOT NULL,
    floor                   VARCHAR(3) NULL,
    apartment_number        VARCHAR(4) NULL,
    FOREIGN KEY (organisation_id)   REFERENCES organisations_schema.organisations(id) ON DELETE CASCADE,
    FOREIGN KEY (city_id)   REFERENCES organisations_schema.cities(id)
);
