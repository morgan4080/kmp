package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class Details(
    val type: String,
    val value: String
)