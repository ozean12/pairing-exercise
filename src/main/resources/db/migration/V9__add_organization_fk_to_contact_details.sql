ALTER TABLE organisations_schema.organisations DROP COLUMN contact_details_id;
ALTER TABLE organisations_schema.contact_details ADD COLUMN organization_id uuid;
ALTER TABLE organisations_schema.contact_details ADD CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organisations_schema.organisations(id);
