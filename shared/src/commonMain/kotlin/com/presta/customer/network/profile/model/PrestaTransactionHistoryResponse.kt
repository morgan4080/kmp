package com.presta.customer.network.profile.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


@Serializable
data class PrestaTransactionHistoryResponse @OptIn(ExperimentalSerializationApi::class) constructor(
     val transactionId:String,
     val purpose: String,
     val amount:Double,
     val transactionDate: String,
     @EncodeDefault val postingType: PostingType? = null,
)

@Serializable
enum class PostingType {
     DR,
     CR
}
