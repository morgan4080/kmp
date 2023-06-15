package com.presta.customer.network.shortTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaShortTermProductsListResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val name: String? = null,
    @EncodeDefault val refId: String? = null,
    @EncodeDefault val interestRate: Double?=null,
    @EncodeDefault val interestPeriodCycle: String?=null,
    @EncodeDefault val isInterestGraduated: Boolean?=null,
    @EncodeDefault val daysAvailable: String?=null,
    @EncodeDefault val loanPeriodUnit: String?=null,
    @EncodeDefault val minTerm: Int?=null,
    @EncodeDefault val maxTerm: Int?=null,
    @EncodeDefault val minAmount: Double?=null,
    @EncodeDefault val maxAmount: Double?=null
)

