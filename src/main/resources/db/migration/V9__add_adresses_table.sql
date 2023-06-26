CREATE TABLE IF NOT EXISTS organisations_schema.addresses
(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    data        json   NOT NULL
);
