package com.presta.customer.network.payments.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PaymentsResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val paymentRequestId: String? = null
)