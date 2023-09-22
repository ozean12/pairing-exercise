
# CDD - adding address to organisation

This document will list decisions and work plan for the address feature.

## Description
This feature adds an address to an organisation. The new organisation create request format is thus:
{

}

And the response:

Portland Pl, London W1A 1AA, UK

## Work Plan
1. Make database schema changes
2. Modify tests to assume feature is implemented (will fail currently)
3. Modify and add models: Organisation{Request, Response}, Address
4. Add input validations
5. Implement repository, service layers
