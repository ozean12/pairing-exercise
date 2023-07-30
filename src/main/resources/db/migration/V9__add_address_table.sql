CREATE TABLE IF NOT EXISTS organisations_schema.address
(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    zipcode     VARCHAR(6),
    street_name VARCHAR(256),
    number      INTEGER,
    city        VARCHAR(256)
);
