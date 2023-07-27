package com.presta.customer.network.signHome.model

import kotlinx.serialization.Serializable


@Serializable
data class Details(
    val value: String,
    val type: String
)