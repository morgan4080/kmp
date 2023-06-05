package com.presta.customer.network.payments.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

enum class PaymentStatuses {
    PENDING, CANCELLED, PROCESSING, COMPLETED, FAILURE
}
@Serializable
data class PrestaPollingResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val status: PaymentStatuses? = null
)