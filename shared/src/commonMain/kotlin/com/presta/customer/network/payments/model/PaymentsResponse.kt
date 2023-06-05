package com.presta.customer.network.payments.model

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentStatuses {
    PENDING, CANCELLED, PROCESSING, COMPLETED, FAILURE
}