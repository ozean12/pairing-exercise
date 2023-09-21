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
                "  }\n" +
                "}"
    }

    fun addressRequestJson(orgId: String): String{
        return "{\n" +
                "  \"city\": \"Gothenburg\",\n" +
                "  \"postcode\": \"06060\",\n" +
                "  \"address_line_1\": \"Gunnar Engellaus väg 8, 418 78\",\n" +
                "  \"address_line_2\": \"\",\n" +
                "  \"organisation_id\": \"$orgId\",\n" +
                "  \"city_id\": \"a6ab3325-2d3d-4a33-a06f-51588dd6c33f\"\n" +
                "}"
    }

    fun addressRequestJsonCityBlank(orgId: String): String{
        return "{\n" +
                "  \"city\": \"\",\n" +
                "  \"postcode\": \"06060\",\n" +
                "  \"address_line_1\": \"Gunnar Engellaus väg 8, 418 78\",\n" +
                "  \"address_line_2\": \"\",\n" +
                "  \"organisation_id\": \"$orgId\",\n" +
                "  \"city_id\": \"a6ab3325-2d3d-4a33-a06f-51588dd6c33f\"\n" +
                "}"
    }

    fun addressRequestJsonNoCity(orgId: String): String{
        return "{\n" +
                "  \"postcode\": \"06060\",\n" +
                "  \"address_line_1\": \"Gunnar Engellaus väg 8, 418 78\",\n" +
                "  \"address_line_2\": \"\",\n" +
                "  \"organisation_id\": \"$orgId\",\n" +
                "  \"city_id\": \"a6ab3325-2d3d-4a33-a06f-51588dd6c33f\"\n" +
                "}"
    }

    fun addressRequestJsonPostcodeBlank(orgId: String): String{
        return "{\n" +
                "  \"city\": \"Gothenburg\",\n" +
                "  \"postcode\": \"\",\n" +
                "  \"address_line_1\": \"Gunnar Engellaus väg 8, 418 78\",\n" +
                "  \"address_line_2\": \"\",\n" +
                "  \"organisation_id\": \"$orgId\",\n" +
                "  \"city_id\": \"a6ab3325-2d3d-4a33-a06f-51588dd6c33f\"\n" +
                "}"
    }

    fun addressRequestJsonNoPostcode(orgId: String): String{
        return "{\n" +
                "  \"city\": \"Gothenburg\",\n" +
                "  \"address_line_1\": \"Gunnar Engellaus väg 8, 418 78\",\n" +
                "  \"address_line_2\": \"\",\n" +
                "  \"organisation_id\": \"$orgId\",\n" +
                "  \"city_id\": \"a6ab3325-2d3d-4a33-a06f-51588dd6c33f\"\n" +
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

    fun bbcAddressFixture(id: UUID, orgId: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = id
        data["city"] = "Gothenburg"
        data["postcode"] = "06060"
        data["address_line_1"] = "Gunnar Engellaus väg 8, 418 78"
        data["address_line_2"] = ""
        data["organisation_id"] = orgId
        data["city_id"] = "a6ab3325-2d3d-4a33-a06f-51588dd6c33f"
        return data
    }

}
