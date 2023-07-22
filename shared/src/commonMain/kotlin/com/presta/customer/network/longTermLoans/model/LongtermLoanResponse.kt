package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class LongtermLoanResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val refId: String?=null,
    @EncodeDefault val name: String?=null,
    @EncodeDefault val interestRate: Double?=null,
    @EncodeDefault val requiredGuarantors: Int?=null,
    @EncodeDefault val roleActions: List<RoleAction>?=null,
    @EncodeDefault val templateId: String?=null,
    @EncodeDefault val templateName: String?=null,
)
