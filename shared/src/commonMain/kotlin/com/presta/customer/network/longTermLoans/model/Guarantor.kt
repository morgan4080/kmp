package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class Guarantor(
    val memberRefId: String,
    val committedAmount: String,
    val guarantorName: String
)
