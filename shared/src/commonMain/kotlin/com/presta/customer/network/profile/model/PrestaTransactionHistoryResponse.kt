package com.presta.customer.network.profile.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaTransactionHistoryResponse @OptIn(ExperimentalSerializationApi::class) constructor(
     val refId: String,
     val transactionId: String,
     val transactionReference: String,
     val amount: Double,
     @EncodeDefault val postingType: PostingType? = null,
     @EncodeDefault val purpose: String? = null,
     val created: String,
     val period: String,
)

@Serializable
enum class PostingType {
     DR,
     CR
}
