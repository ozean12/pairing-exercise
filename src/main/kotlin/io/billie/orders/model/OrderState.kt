package io.billie.orders.model

enum class OrderState {
    NEW, //announced
    SHIPPED, //shipped by merchant
    MERCHANT_PAID,
    INVOICED, //the buyer is invoiced
    COMPLETED, //the buyer paid the invoice, final state
    CANCELED //the order was cancelled (for example, was never shipped)
}
