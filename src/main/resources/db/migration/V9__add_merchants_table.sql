CREATE TABLE IF NOT EXISTS organisations_schema.merchants
(
    id                      UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name                    VARCHAR(200) NOT NULL
);
