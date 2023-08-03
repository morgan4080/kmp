package com.presta.customer.network.longTermLoans.tst

import kotlinx.serialization.Serializable

@Serializable
data class Guarantor(
    val committedAmount: Int,
    val memberRefId: String
)