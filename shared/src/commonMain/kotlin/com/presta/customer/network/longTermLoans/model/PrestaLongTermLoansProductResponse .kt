package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaLongTermLoansProductResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val list: List<LongTermLoanResponse>?=null,
    @EncodeDefault val total: Int?=null
)
