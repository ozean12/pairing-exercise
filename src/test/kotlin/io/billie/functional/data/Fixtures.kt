package io.billie.functional.data

import java.text.SimpleDateFormat
import java.util.UUID

object Fixtures {

    fun orgRequestJsonNameBlank(): String {
        return """{
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
                }"""
    }

    fun orgRequestJsonNoName(): String {
        return """{
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
                }"""
    }

    fun orgRequestJsonNoLegalEntityType(): String {
        return """{
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
                }"""
    }

    fun orgRequestJsonNoContactDetails(): String {
        return """{
          "name": "BBC",
          "date_founded": "18/10/1922",
          "country_code": "GB",
          "vat_number": "333289454",
          "registration_number": "3686147",
          "legal_entity_type": "NONPROFIT_ORGANIZATION"
        }"""
    }

    fun orgRequestJson(): String {
        return """{
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
                        "address1": "Portland Pl",
                        "address2": "",
                        "country_code": "GB"
                        "city": "London"
                        "state": "Greater London"
                        "postal_code": "W1A 1AA"
                  }
                }"""
    }

    fun orgRequestJsonCountryCodeBlank(): String {
        return """{
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
                  "address": {
                    "address1": "Portland Pl",
                    "address2": "",
                    "country_code": "GB"
                    "city": "London"
                    "state": "Greater London"
                    "postal_code": "W1A 1AA"
                  }
                }"""
    }

    fun orgRequestJsonNoCountryCode(): String {
        return """{
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
                    "address1": "Portland Pl",
                    "address2": "",
                    "country_code": "GB"
                    "city": "London"
                    "state": "Greater London"
                    "postal_code": "W1A 1AA"
                  }
                }"""
    }

    fun orgRequestJsonCountryCodeIncorrect(): String {
        return """{
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
                  "address": {
                    "address1": "Portland Pl",
                    "address2": "",
                    "country_code": "GB"
                    "city": "London"
                    "state": "Greater London"
                    "postal_code": "W1A 1AA"
                  }
                }"""
    }

    fun orgRequestJsonAddressMissing(): String {
        return """{
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
                }"""
    }

    fun orgRequestJsonAddressCountryInvalid(): String {
        return """{
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
                    "address1": "Portland Pl",
                    "address2": "",
                    "country_code": "XX"
                    "city": "London"
                    "state": "Greater London"
                    "postal_code": "W1A 1AA"
                  }
                }"""
    }

    fun orgRequestJsonCityInvalid(): String {
        return """{
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
                    "address1": "Portland Pl",
                    "address2": "",
                    "country_code": "GB"
                    "city": "Laondon"
                    "state": "Greater London"
                    "postal_code": "W1A 1AA"
                  }
                }"""
    }

    fun orgResponseJson(id: String, countryId: String, contactDetailsId: String, addressId:String): String =
        """
        [{
            "id": "$id",
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
                "address1": "Portland Pl",
                "address2": "",
                "country_code": "GB"
                "city": "London"
                "state": "Greater London"
                "postal_code": "W1A 1AA"
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

    fun bbcAddressFixture(id: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = id
        data["address1"] = "Portland Pl"
        data["address2"] = ""
        data["country_code"] = "GB"
        data["city"] = "London"
        data["state"] = "Greater London"
        data["postal_code"] = "W1A 1AA"
        return data
    }

}
