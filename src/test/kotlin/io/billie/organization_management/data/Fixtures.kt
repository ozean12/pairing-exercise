package io.billie.organization_management.data

object Fixtures {

    fun orgRequestJsonNameBlank(): String {
        return """{
                    "name": "",
                    "date_founded": "18/10/1922",
                    "country_code": "GB",
                    "vat_number": "333289454",
                    "registration_number": "3686147",
                    "legal_entity_type": "NONPROFIT_ORGANIZATION",
                    "contact_detail": {
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
                    "contact_detail": {
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
                    "contact_detail": {
                        "phone_number": "+443700100222",
                        "fax": "",
                        "email": "yourquestions@bbc.co.uk"
                    }
                }"""
    }

    fun orgRequestJsonNoContactDetails(): String {
        return """{
                    "name": "",
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
                    "contact_detail": {
                        "phone_number": "+443700100222",
                        "fax": "",
                        "email": "yourquestions@bbc.co.uk"
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
                    "contact_detail": {
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
                    "contact_detail": {
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
                    "contact_detail": {
                        "phone_number": "+443700100222",
                        "fax": "",
                        "email": "yourquestions@bbc.co.uk"
                    }
                }"""
    }

    fun addressRequestJson(): String {
        return """{
                    "city_id": "363a1c53-646a-4bc2-997b-9ce29c2d2f29",
                    "street": "Hafez",
                    "number": "111-113",
                    "postal_code": "14560"
                }"""
    }
}
