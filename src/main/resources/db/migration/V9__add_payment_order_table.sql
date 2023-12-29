CREATE TABLE organisations_schema.payment_order
(
    id               uuid                 NOT NULL,
    organisation_id  uuid                 NOT NULL,
    items_count      bigint               NOT NULL,
    total_price      double precision     NOT NULL,
    currency_code    character varying(3) NOT NULL,
    remaining_amount double precision     NOT NULL DEFAULT 0.0,
    created_at       timestamptz          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       timestamptz          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);