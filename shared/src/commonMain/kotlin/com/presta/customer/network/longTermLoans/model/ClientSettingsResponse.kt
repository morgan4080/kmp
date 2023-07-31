package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientSettingsResponse(
    val response: Response
)