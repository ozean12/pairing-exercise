package io.billie.functional.data

import java.util.UUID

object OrderFixtures {

    fun simpleValidOrderRequest() = simpleValidOrderRequest(UUID.randomUUID(), UUID.randomUUID())

    fun simpleValidOrderRequest(orderId: UUID) = simpleValidOrderRequest(orderId, UUID.randomUUID())

    fun simpleValidOrderRequestWithOrganisation(organisationId: UUID) =
        simpleValidOrderRequest(UUID.randomUUID(), organisationId)

    fun simpleValidOrderRequest(orderId: UUID, organisationId: UUID) = """
        {
            "order_id": "$orderId",
            "total_gross": 750.00,
            "organisation_id": "$organisationId",
            "customer": {
              "name": "CocaComany",
              "vat_number": "234252515315",
              "address": "Kurfurstendamm 237, 10719 Berlin"
            },
            "products": [
              {
                "name": "Red Cup",
                "price": 100.00,
                "quantity": 2
              },
              {
                "name": "Blue Cup",
                "price": 110.00,
                "quantity": 5
              }
            ]
        }
    """.trimIndent()


    fun notValidOrder() = """
        {
           "order_id": "some_id",
           "total_gross": 750.00, 
           "organisation_id": "my_company"
        }
    """.trimIndent()

    fun simpleOrderRequestWithExtraField() = """
        {
            "additional_field": "random",
            "order_id": "doesnt_matter",
            "total_gross": 750.00,
            "organisation_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            "customer": {
              "name": "CocaComany",
              "vat_number": "234252515315",
              "address": "Kurfurstendamm 237, 10719 Berlin"
            },
            "products": [
              {
                "name": "Red Cup",
                "price": 100.00,
                "quantity": 2
              },
              {
                "name": "Blue Cup",
                "price": 110.00,
                "quantity": 5
              }
            ]
        }
    """.trimIndent()
}