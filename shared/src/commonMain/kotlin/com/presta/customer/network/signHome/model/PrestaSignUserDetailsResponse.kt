package com.presta.customer.network.signHome.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaSignUserDetailsResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val isTermsAccepted: Boolean,
    val refId: String,
    val created: String,
    val createdBy: String,
    val firstName: String,
    val fullName: String,
    val lastName: String,
    @EncodeDefault val idNumber: String = "",
    @EncodeDefault val memberNumber: String = "",
    val phoneNumber: String,
    @EncodeDefault val email: String = "",
    val totalShares: Double,
    val totalDeposits: Double,
    @EncodeDefault val committedAmount: Double = 0.0,
    @EncodeDefault val availableAmount: Double = 0.0,
    val memberStatus: String,
    @EncodeDefault val details: Map<String,Details>?=null
)