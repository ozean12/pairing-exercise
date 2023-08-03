package io.billie.organisations.data

data class InvalidAddress(
    val fieldName: String,
    val error: String
) : RuntimeException(
    "Error in field $fieldName: $error"
)
