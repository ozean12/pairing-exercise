package io.billie.functional.data

object OrderFixtures {

    fun orderRequestJson(): String {
        return "{\n" +
                "  \"amount\": 20.0\n" +
                "}"
    }

    fun orderRequestJsonMissingAmount(): String {
        return "{\n" +
                "}"
    }

    fun orderRequestJsonInvalidAmount(): String {
        return "{\n" +
                "  \"amount\": -1\n" +
                "}"
    }
}