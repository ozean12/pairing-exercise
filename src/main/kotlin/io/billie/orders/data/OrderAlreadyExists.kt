package io.billie.orders.data

class OrderAlreadyExists(val external_id: String) : RuntimeException()
