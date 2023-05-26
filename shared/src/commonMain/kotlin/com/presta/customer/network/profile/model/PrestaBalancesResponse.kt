package com.presta.customer.network.profile.model

import kotlinx.serialization.Serializable

// {"savingsBalance":0.00,"sharesBalance":0.00,"sharesCount":0,"savingsTotalAmount":0.00,"pricePerShare":"100.0"}
@Serializable
data class  PrestaBalancesResponse(
    val savingsBalance:Double,
    val sharesBalance: Double,
    val sharesCount: Int,
    val savingsTotalAmount: Double,
    val pricePerShare: Double
)