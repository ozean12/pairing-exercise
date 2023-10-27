CREATE TABLE IF NOT EXISTS organisations_schema.addresses
(
    id                      UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    city                    VARCHAR(100) NOT NULL,
    postcode                VARCHAR(20)  NOT NULL,
    address_line_1          VARCHAR(100) NOT NULL,
    address_line_2          VARCHAR(100),
    organisation_id         UUID,
    city_id                 UUID,
    timestamp timestamp default current_timestamp
);

ALTER TABLE organisations_schema.addresses
    ADD CONSTRAINT constraint_organisation_fk
        FOREIGN KEY (organisation_id)
            REFERENCES organisations_schema.organisations(id)
            ON DELETE CASCADE;

CREATE INDEX idx_address_fk
    ON organisations_schema.addresses (organisation_id);