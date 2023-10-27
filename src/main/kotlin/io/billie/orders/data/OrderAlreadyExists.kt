package io.billie.orders.data

class OrderAlreadyExists(val externalId: String) : RuntimeException()
