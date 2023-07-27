package com.presta.customer.network.signHome.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaSignUserDetailsResponse constructor(
    val isTermsAccepted: Boolean,
    val refId: String,
    val created: String,
    val createdBy: String,
    val firstName: String,
    val fullName: String,
    val lastName: String,
    val idNumber: String,
    val memberNumber: String,
    val phoneNumber: String,
    val email: String,
    val totalShares: Double,
    val totalDeposits: Double,
    val committedAmount: Double,
    val availableAmount: Double,
    val memberStatus: String,
    val details: Map<String,Details>
)