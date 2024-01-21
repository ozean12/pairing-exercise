CREATE TABLE IF NOT EXISTS organisations_schema.orders
(
    id                  UUID    DEFAULT  uuid_generate_v4() PRIMARY KEY,
    organisation_id     UUID    REFERENCES organisations_schema.organisations,
    amount              MONEY   NOT NULL
);
