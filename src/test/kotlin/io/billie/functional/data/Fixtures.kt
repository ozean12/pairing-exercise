package io.billie.functional.data

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
                  }
                }"""
    }

    fun orgRequestWithAddressJson(cityId: String): String {
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
                    "city_id": "$cityId",
                    "zip_code": "1046AC",
                    "street": "Jarmuiden",
                    "street_number": "31",
                    "apartment_number": "4A"
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
                  }
                }"""
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

    fun bbcAddressFixture(id: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = id
        data["zip_code"] = "1046AC"
        data["street"] = "Jarmuiden"
        data["street_number"] = "31"
        data["apartment_number"] = "4A"
        return data
    }

    fun addressRequestJson(cityId: String): String {
        return """{
                  "city_id": "$cityId",
                  "zip_code": "1046AC",
                  "street": "Jarmuiden",
                  "street_number": "31",
                  "apartment_number": "4A"
                }"""
    }

}
