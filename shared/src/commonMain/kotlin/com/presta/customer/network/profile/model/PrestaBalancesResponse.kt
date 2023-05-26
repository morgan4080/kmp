package com.presta.customer.network.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class  PrestaBalancesResponse(
    val savingsBalance:Double,
    val sharesBalance: Double,
    val sharesCount:Int,
    val savingsTotalAmount: Double,
    val pricePerShare:Int
)