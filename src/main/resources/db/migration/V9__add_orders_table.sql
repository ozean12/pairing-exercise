CREATE TABLE IF NOT EXISTS organisations_schema.orders
(
    id                  UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    external_id         VARCHAR     NOT NULL,
    created_time        TIMESTAMP   NOT NULL,
    organisation_id     UUID NOT NULL,
    state               VARCHAR(30) NOT NULL
);
