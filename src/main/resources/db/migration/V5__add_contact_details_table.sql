
CREATE TABLE IF NOT EXISTS organisations_schema.contact_details
(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    phone_number VARCHAR(20),
    fax         VARCHAR(20),
    email       VARCHAR(256)
);
