ALTER TABLE organisations_schema.organisations
    ADD COLUMN IF NOT EXISTS address_details_id VARCHAR(36);