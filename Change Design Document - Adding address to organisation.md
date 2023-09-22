
# CDD - adding address to organisation

This document will list decisions and work plan for the address feature.

## Description
This feature adds an address to an organisation.

### Model
Address data is stored in a new `address` table, pointed to by the new `address_id` field in organisation.
That means an organisation can have exactly one address.

### Organisation create format
```json
{
  ... existing fields
  "address": {
    "address1": "Portland Pl",
    "address2": "",
    "country_code": "GB",
    "city": "London",
    "state": "Greater London",
    "postal_code": "W1A 1AA"
  }
}
```

### Validations
* Country exists in database
* City exists in database
* `address1`, `country_code`, `city`, `postal_code` not empty or null

### Organisation list response format
```json
{
  ... existing fields
  "address": {
    "id": "<some guid>",
    "address1": "Portland Pl",
    "address2": "",
    "country_code": "GB",
    "city": "London",
    "state": "Greater London",
    "postal_code": "W1A 1AA"
  }
}
```

## Work Plan
1. Make database schema changes
2. Modify tests to assume feature is implemented (will fail currently)
3. Modify and add models: Organisation{Request, Response}, Address
4. Add input validations
5. Implement repository, service layers
