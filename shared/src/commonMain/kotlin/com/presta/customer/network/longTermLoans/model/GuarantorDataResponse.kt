package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class GuarantorDataResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val refId: String,
    val memberNumber: String,
    val memberRefId: String,
    val firstName: String,
    val lastName: String,
    val eligible: Boolean,
    val eligibilityMessage: String,
    val isAccepted: Boolean,
    val isSigned: Boolean,
    val isActive: Boolean,
    val committedAmount: Double,
    @EncodeDefault val isApproved: Boolean?= null,
)

