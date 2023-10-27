
CREATE TABLE IF NOT EXISTS organisations_schema.contact_details
(
    id                  UUID DEFAULT    uuid_generate_v4() PRIMARY KEY,
    phone_number        VARCHAR(20)     NOT NULL,
    fax                 VARCHAR(20)     NOT NULL,
    email               VARCHAR(256)    NOT NULL
);
