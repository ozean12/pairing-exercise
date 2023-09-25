CREATE TABLE IF NOT EXISTS organisations_schema.products
(
    id    UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name  VARCHAR(256) NOT NULL,
    price DECIMAL(12, 2) NOT NULL
);
