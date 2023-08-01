ALTER TABLE organisations_schema.organisations
    ADD COLUMN address_id UUID REFERENCES organisations_schema.addresses(id);
