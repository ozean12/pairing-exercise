package io.billie.functional.data

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object Fixtures {

    fun orgRequestJsonNameBlank(): String {
        return "{\n" +
                "  \"name\": \"\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoName(): String {
        return "{\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoLegalEntityType(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoContactDetails(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\"\n" +
                "}"
    }

    fun orgRequestJson() = """
            {
              "name": "BBC",
              "date_founded": "18/10/1922",
              "country_code": "GB",
              "vat_number": "333289454",
              "registration_number": "3686147",
              "legal_entity_type": "NONPROFIT_ORGANIZATION",
              "contact_details": {
                "phone_number": "+443700100222",
                "fax": "",
                "email": "yourquestions@bbc.co.uk"
              },
              "address" : {
                "city": "London",
                "street_and_number": "101 Wood Lane",
                "postal_code": "W12 7FA"
              }
            }
        """.trimIndent()

    fun orgRequestJsonCountryCodeBlank() = """
            {
              "name": "BBC",
              "date_founded": "18/10/1922",
              "country_code": "",
              "vat_number": "333289454",
              "registration_number": "3686147",
              "legal_entity_type": "NONPROFIT_ORGANIZATION",
              "contact_details": {
                "phone_number": "+443700100222",
                "fax": "",
                "email": "yourquestions@bbc.co.uk"
              },
              "address" : {
                "city": "London",
                "street_and_number": "101 Wood Lane",
                "postal_code": "W12 7FA"
              }
            }
        """.trimIndent()

    fun orgRequestJsonNoCountryCode() = """
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
              "address" : {
                "city": "London",
                "street_and_number": "101 Wood Lane",
                "postal_code": "W12 7FA"
              }
            }
        """.trimIndent()

    fun orgRequestJsonCountryCodeIncorrect() = """
            {
              "name": "BBC",
              "date_founded": "18/10/1922",
              "country_code": "XX",
              "vat_number": "333289454",
              "registration_number": "3686147",
              "legal_entity_type": "NONPROFIT_ORGANIZATION",
              "contact_details": {
                "phone_number": "+443700100222",
                "fax": "",
                "email": "yourquestions@bbc.co.uk"
              },
              "address" : {
                "city": "London",
                "street_and_number": "101 Wood Lane",
                "postal_code": "W12 7FA"
              }
            }
        """.trimIndent()

    fun orgRequestJsonNoAddress() = """
            {
              "name": "BBC",
              "date_founded": "18/10/1922",
              "country_code": "GB",
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

    fun orgRequestJsonNoAddressStreet() = """
            {
              "name": "BBC",
              "date_founded": "18/10/1922",
              "country_code": "GB",
              "vat_number": "333289454",
              "registration_number": "3686147",
              "legal_entity_type": "NONPROFIT_ORGANIZATION",
              "contact_details": {
                "phone_number": "+443700100222",
                "fax": "",
                "email": "yourquestions@bbc.co.uk"
              },
              "address" : {
                "city": "London",
                "postal_code": "W12 7FA"
              }
            }
    """.trimIndent()

    fun orgRequestJsonNoAddressPostalCode() = """
            {
              "name": "BBC",
              "date_founded": "18/10/1922",
              "country_code": "GB",
              "vat_number": "333289454",
              "registration_number": "3686147",
              "legal_entity_type": "NONPROFIT_ORGANIZATION",
              "contact_details": {
                "phone_number": "+443700100222",
                "fax": "",
                "email": "yourquestions@bbc.co.uk"
              },
              "address" : {
                "city": "London",
                "street_and_number": "101 Wood Lane"
              }
            }
        """.trimIndent()

    fun orgRequestJsonAddressCityIncorrect() = """
            {
              "name": "BBC",
              "date_founded": "18/10/1922",
              "country_code": "GB",
              "vat_number": "333289454",
              "registration_number": "3686147",
              "legal_entity_type": "NONPROFIT_ORGANIZATION",
              "contact_details": {
                "phone_number": "+443700100222",
                "fax": "",
                "email": "yourquestions@bbc.co.uk"
              },
              "address" : {
                "city": "XXX",
                "street_and_number": "101 Wood Lane",
                "postal_code": "W12 7FA"
              }
            }
    """.trimIndent()

    fun orgRequestJsonAddressCityNotBelongingToCountry() = """
            {
              "name": "BBC",
              "date_founded": "18/10/1922",
              "country_code": "GB",
              "vat_number": "333289454",
              "registration_number": "3686147",
              "legal_entity_type": "NONPROFIT_ORGANIZATION",
              "contact_details": {
                "phone_number": "+443700100222",
                "fax": "",
                "email": "yourquestions@bbc.co.uk"
              },
              "address" : {
                "city": "Berlin",
                "street_and_number": "101 Wood Lane",
                "postal_code": "W12 7FA"
              }
            }
    """.trimIndent()

    fun orgRequestJsonAddressCityBlank() = """
            {
              "name": "BBC",
              "date_founded": "18/10/1922",
              "country_code": "GB",
              "vat_number": "333289454",
              "registration_number": "3686147",
              "legal_entity_type": "NONPROFIT_ORGANIZATION",
              "contact_details": {
                "phone_number": "+443700100222",
                "fax": "",
                "email": "yourquestions@bbc.co.uk"
              },
              "address" : {
                "city": "",
                "street_and_number": "101 Wood Lane",
                "postal_code": "W12 7FA"
              }
            }
    """.trimIndent()


    fun orgResponseJson(orgId: String, countryId: String, contactDetailsId: String, addressId:String) = """
        [{
            "id": "$orgId",
            "name": "BBC",
            "date_founded": "18/10/1922",
            "country": {
                "id": "$countryId",
                "name": "United Kingdom",
                "country_code": "GB"
            },
            "registration_number": "3686147",
            "legal_entity_type": "NONPROFIT_ORGANIZATION",
            "contact_details": {
                "id": "$contactDetailsId",
                "phone_number": "+443700100222",
                "fax": "",
                "email": "yourquestions@bbc.co.uk"
            },
            "address": {
                "id": "$addressId",
                "city": "London",
                "postal_code": "W12 7FA",
                "street_and_number": "101 Wood Lane"
            },
            "vatnumber": "333289454"
        }]
        """.trimIndent()
    fun bbcFixture(id: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = id
        data["name"] = "BBC"
        data["date_founded"] = SimpleDateFormat("yyyy-MM-dd").parse("1922-10-18")
        data["country_code"] = "GB"
        data["vat_number"] = "333289454"
        data["registration_number"] = "3686147"
        data["legal_entity_type"] = "NONPROFIT_ORGANIZATION"
        return data
    }

    fun bbcContactFixture(id: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = id
        data["phone_number"] = "+443700100222"
        data["fax"] = ""
        data["email"] = "yourquestions@bbc.co.uk"
        return data
    }

    fun bbcAddressFixture(id: UUID) = mapOf(
        "id" to id,
        "city" to "London",
        "street_and_number" to "101 Wood Lane",
        "postal_code" to "W12 7FA"
    )



}
