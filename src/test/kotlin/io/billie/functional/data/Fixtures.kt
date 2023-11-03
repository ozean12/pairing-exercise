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

    fun orgRequestNoAddress(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
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

    fun orgRequestAddressCountryCodeBlank(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  },\n" +
                "  \"address\": {\n" +
                "    \"country_code\": \"\",\n" +
                "    \"city\": \"Lake City\",\n" +
                "    \"street\": \"NE Leon St\",\n" +
                "    \"house_number\": \"156\",\n" +
                "    \"postal_code\": \"FL 32055\",\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestAddressNoCountryCode(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"city\": \"Lake City\",\n" +
            "    \"street\": \"NE Leon St\",\n" +
            "    \"house_number\": \"156\",\n" +
            "    \"postal_code\": \"FL 32055\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressCountryCodeIsIncorrect(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US123\",\n" +
            "    \"city\": \"Lake City\",\n" +
            "    \"street\": \"NE Leon St\",\n" +
            "    \"house_number\": \"156\",\n" +
            "    \"postal_code\": \"FL 32055\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressCityBlank(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US\",\n" +
            "    \"city\": \"\",\n" +
            "    \"street\": \"NE Leon St\",\n" +
            "    \"house_number\": \"156\",\n" +
            "    \"postal_code\": \"FL 32055\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressNoCity(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US\",\n" +
            "    \"street\": \"NE Leon St\",\n" +
            "    \"house_number\": \"156\",\n" +
            "    \"postal_code\": \"FL 32055\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressCityIsIncorrect(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US\",\n" +
            "    \"city\": \"Lake City123\",\n" +
            "    \"street\": \"NE Leon St\",\n" +
            "    \"house_number\": \"156\",\n" +
            "    \"postal_code\": \"FL 32055\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressStreetBlank(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US\",\n" +
            "    \"city\": \"Lake City123\",\n" +
            "    \"street\": \"\",\n" +
            "    \"house_number\": \"156\",\n" +
            "    \"postal_code\": \"FL 32055\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressNoStreet(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US\",\n" +
            "    \"city\": \"Lake City\",\n" +
            "    \"house_number\": \"156\",\n" +
            "    \"postal_code\": \"FL 32055\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressHouseNumberBlank(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US\",\n" +
            "    \"city\": \"Lake City123\",\n" +
            "    \"street\": \"NE Leon St\",\n" +
            "    \"house_number\": \"\",\n" +
            "    \"postal_code\": \"FL 32055\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressNoHouseNumber(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US\",\n" +
            "    \"city\": \"Lake City123\",\n" +
            "    \"street\": \"NE Leon St\",\n" +
            "    \"postal_code\": \"FL 32055\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressPostalCodeBlank(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US\",\n" +
            "    \"city\": \"Lake City123\",\n" +
            "    \"street\": \"NE Leon St\",\n" +
            "    \"house_number\": \"156\",\n" +
            "    \"postal_code\": \"\",\n" +
            "  }\n" +
            "}"
    }

    fun orgRequestAddressNoPostalCode(): String {
        return "{\n" +
            "  \"name\": \"BBC\",\n" +
            "  \"date_founded\": \"18/10/1922\",\n" +
            "  \"country_code\": \"GB\",\n" +
            "  \"vat_number\": \"333289454\",\n" +
            "  \"registration_number\": \"3686147\",\n" +
            "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
            "  \"contact_details\": {\n" +
            "    \"phone_number\": \"+443700100222\",\n" +
            "    \"fax\": \"\",\n" +
            "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
            "  },\n" +
            "  \"address\": {\n" +
            "    \"country_code\": \"US\",\n" +
            "    \"city\": \"Lake City123\",\n" +
            "    \"street\": \"NE Leon St\",\n" +
            "    \"house_number\": \"156\",\n" +
            "  }\n" +
            "}"
    }


    fun orgRequestJson(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  },\n" +
                "  \"address\": {\n" +
                "    \"country_code\": \"US\",\n" +
                "    \"city\": \"Lake City\",\n" +
                "    \"street\": \"NE Leon St\",\n" +
                "    \"house_number\": \"156\",\n" +
                "    \"postal_code\": \"FL 32055\",\n" +
                "    \"additional_info\": \"some info\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonCountryCodeBlank(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"\",\n" +
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

    fun orgRequestJsonNoCountryCode(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
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

    fun orgRequestJsonCountryCodeIncorrect(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"XX\",\n" +
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
        data["address_country_code"] = "US"
        data["address_city"] = "Lake City"
        data["street"] = "NE Leon St"
        data["house_number"] = "156"
        data["postal_code"] = "FL 32055"
        data["additional_info"] = "some info"
        return data
    }
}
