CREATE TABLE IF NOT EXISTS organisations_schema.organisations
(
    id                 UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name               VARCHAR     NOT NULL,
    date_founded        DATE        NOT NULL,
    country_code        CHAR(2)     NOT NULL,
    VAT_number          VARCHAR(20),
    registration_number VARCHAR(20),
    legal_entity_type    VARCHAR(30) NOT NULL,
    contact_details_id   VARCHAR(36) NOT NULL,
    address_details_id   VARCHAR(36) NOT NULL
);
