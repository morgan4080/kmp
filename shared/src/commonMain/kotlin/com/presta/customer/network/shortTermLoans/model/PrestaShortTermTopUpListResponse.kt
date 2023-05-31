package com.presta.customer.network.shortTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaShortTermTopUpListResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val loans: List<PrestaTopUpLoansList>?=null
)