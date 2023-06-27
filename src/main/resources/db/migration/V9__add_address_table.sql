
CREATE TABLE IF NOT EXISTS organisations_schema.address
(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    street_name VARCHAR(20),
    home_number VARCHAR(20),
    zip_code    VARCHAR(6)
);
