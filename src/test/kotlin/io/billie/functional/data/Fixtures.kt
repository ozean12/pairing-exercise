package io.billie.functional.data

import java.text.SimpleDateFormat
import java.util.UUID

object Fixtures {

    fun orgRequestJsonNameBlank() =
        // language=json
        """
           {
             "name": "",
             "date_founded": "18/10/1922",
             "vat_number": "333289454",
             "registration_number": "3686147",
             "legal_entity_type": "NONPROFIT_ORGANIZATION",
             "contact_details": {
               "phone_number": "+443700100222",
               "fax": "",
               "email": "yourquestions@bbc.co.uk"
             },
              "address": {
               "street": "Television Centre",
               "house_number": "1",
               "additional": "additional",
               "zip_code": "W12 7FA",
               "city": "London",
               "country_code": "GB"
             }
           }
        """.trimIndent()

    fun orgRequestJsonNoName() =
        // language=json
        """
    {
      "date_founded": "18/10/1922",
      "vat_number": "333289454",
      "registration_number": "3686147",
      "legal_entity_type": "NONPROFIT_ORGANIZATION",
      "contact_details": {
        "phone_number": "+443700100222",
        "fax": "",
        "email": "yourquestions@bbc.co.uk"
      },
      "address": {
        "address_line_1": "Television Centre",
        "address_line_2": "1",
        "zip_code": "W12 7FA",
        "city": "London",
        "country_code": "GB"
      }
    }
    """.trimIndent()

    fun orgRequestJsonNoLegalEntityType() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "Television Centre",
            "address_line_2": "1",
            "zip_code": "W12 7FA",
            "city": "London",
            "country_code": "GB"
          }
        }
    """.trimIndent()


    fun orgRequestJsonNoContactDetails() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION"
        }
        """.trimIndent()

    fun orgRequestJson() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "Television Centre",
            "address_line_2": "1",
            "zip_code": "W12 7FA",
            "city": "London",
            "country_code": "GB"
          }
        }
    """.trimIndent()


    fun orgRequestJsonCountryCodeBlank() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
              "address_line_1": "Television Centre",
              "address_line_2": "1",
              "zip_code": "W12 7FA",
              "city": "London",
              "country_code": ""
          }
        }
    """.trimIndent()

    fun orgRequestJsonCountryCodeIncorrect() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "Television Centre",
            "address_line_2": "1",
            "zip_code": "W12 7FA",
            "city": "London",
            "country_code": "XX"
          }
        }
    """.trimIndent()

    fun orgRequestJsonAddressMissing() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          }
        }
    """.trimIndent()

    fun orgRequestJsonAddressCityInvalid() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "Television Centre",
            "address_line_2": "1",
            "zip_code": "W12 7FA",
            "city": "does-not-exist",
            "country_code": "GB"
          }
        }
    """.trimIndent()

    fun orgRequestJsonAddressWithNoCountryCode() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
              "address_line_1": "Television Centre",
              "address_line_2": "1",
              "zip_code": "W12 7FA",
              "city": "London"
          }
        }
    """.trimIndent()

    fun orgRequestJsonAddressLine1IsBlank() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "",
            "address_line_2": "1",
            "zip_code": "W12 7FA",
            "city": "London",
            "country_code": "GB"
          }
        }
    """.trimIndent()

    fun orgRequestJsonAddressLine2IsBlank() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "Television Centre",
            "address_line_2": "",
            "zip_code": "W12 7FA",
            "city": "London",
            "country_code": "GB"
          }
        }
    """.trimIndent()

    fun orgRequestJsonAddressLine2IsMissing() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "Television Centre",
            "zip_code": "W12 7FA",
            "city": "London",
            "country_code": "GB"
          }
        }
    """.trimIndent()

    fun orgRequestJsonAddressZipCodeIsBlank() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "Television Centre",
            "address_line_2": "1",
            "zip_code": "",
            "city": "London",
            "country_code": "GB"
          }
        }
    """.trimIndent()

    fun orgRequestJsonAddressCityIsBlank() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "Television Centre",
            "address_line_2": "",
            "zip_code": "W12 7FA",
            "city": "",
            "country_code": "GB"
          }
        }
    """.trimIndent()

    fun orgRequestJsonAddressCountryCodeIsBlank() =
        // language=json
        """
        {
          "name": "BBC",
          "date_founded": "18/10/1922",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION",
          "contact_details": {
            "phone_number": "+443700100222",
            "fax": "",
            "email": "yourquestions@bbc.co.uk"
          },
          "address": {
            "address_line_1": "Television Centre",
            "address_line_2": "",
            "zip_code": "W12 7FA",
            "city": "London",
            "country_code": ""
          }
        }
    """.trimIndent()

    fun bbcFixture(id: UUID): Map<String, Any> =
        HashMap<String, Any>()
            .apply {
                put("id", id)
                put("name", "BBC")
                put("date_founded", SimpleDateFormat("yyyy-MM-dd").parse("1922-10-18"))
                put("country_code", "GB")
                put("vat_number", "333289454")
                put("registration_number", "3686147")
                put("legal_entity_type", "NONPROFIT_ORGANIZATION")
            }

    fun bbcContactFixture(id: UUID): Map<String, Any> =
        HashMap<String, Any>()
            .apply {
                put("id", id)
                put("phone_number", "+443700100222")
                put("fax", "")
                put("email", "yourquestions@bbc.co.uk")
            }

    fun bbcAddressFixture(id: UUID, useLine2: Boolean = true): Map<String, Any> =
        HashMap<String, Any>()
            .apply {
                put("id", id)
                put("address_line_1", "Television Centre")
                if(useLine2) {
                    put("address_line_2", "1")
                }
                put("zip_code", "W12 7FA")
                put("city", "London")
                put("country_code", "GB")
            }
}
