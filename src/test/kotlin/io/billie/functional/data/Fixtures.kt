package io.billie.functional.data

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object Fixtures {

    fun orgRequestJsonNameBlank(): String {
        return """
            {
                "name": "",
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
    }

    fun orgRequestJsonNoName(): String {
        return """
            {
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
    }

    fun orgRequestJsonNoLegalEntityType(): String {
        return """
            {
                "name": "BBC",
                "date_founded": "18/10/1922",
                "country_code": "GB",
                "vat_number": "333289454",
                "registration_number": "3686147",
                "contact_details": {
                    "phone_number": "+443700100222",
                    "fax": "",
                    "email": "yourquestions@bbc.co.uk"
                }
            }
        """.trimIndent()
    }

    fun orgRequestJsonNoContactDetails(): String {
        return """
            {
                "name": "BBC",
                "date_founded": "18/10/1922",
                "country_code": "GB",
                "vat_number": "333289454",
                "registration_number": "3686147",
                "legal_entity_type": "NONPROFIT_ORGANIZATION"
            }
        """.trimIndent()
    }

    fun orgRequestJsonNoAddres(): String {
        return """
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
    }

    fun orgRequestJson(): String {
        return """
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
                "address": {
                    "line1": "BBC street 1",
                    "line2": "",
                    "post_code": "123456",
                    "city": "London",
                    "country_code": "GB"
                }
            }
        """.trimIndent()
    }

    fun orgRequestJsonCountryCodeBlank(): String {
        return """
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
                }
            }
        """.trimIndent()
    }

    fun orgRequestJsonNoCountryCode(): String {
        return """
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
    }

    fun orgRequestJsonCountryCodeIncorrect(): String {
        return """
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
                }
            }
        """.trimIndent()
    }

    fun orgRequestJsonAddressCountryCodeIncorrect(): String {
        return """
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
                "address": {
                    "line1": "BBC street 1",
                    "line2": "",
                    "post_code": "123456",
                    "city": "London",
                    "country_code": "XX"
                }
            }
        """.trimIndent()
    }

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

    fun bbcAddressFixture(): Map<String, Any> {
        val data:HashMap<String, Any> = hashMapOf("line1" to "BBC street 1",
            "line2" to "",
            "city" to "London",
            "country_code" to "GB",
            "post_code" to "123456")
        return data
    }



}
