CREATE TABLE IF NOT EXISTS organisations_schema.organisations
(
    id                      UUID        DEFAULT uuid_generate_v4() PRIMARY KEY,
    name                    VARCHAR     NOT NULL,
    date_founded            DATE        NOT NULL,
    country_id              UUID        NOT NULL references organisations_schema.countries(id),
    vat_number              VARCHAR(20),
    registration_number     VARCHAR(20),
    legal_entity_type       VARCHAR(30) NOT NULL,
    contact_detail_id       UUID        NOT NULL references organisations_schema.contact_details(id)
);
