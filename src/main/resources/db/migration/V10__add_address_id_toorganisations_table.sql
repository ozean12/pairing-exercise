ALTER TABLE organisations_schema.organisations
    ADD address_id UUID references organisations_schema.addresses(id);
