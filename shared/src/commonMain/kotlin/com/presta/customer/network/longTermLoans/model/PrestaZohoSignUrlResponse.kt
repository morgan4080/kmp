package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaZohoSignUrlResponse(
    val signURL: String,
    val success: Boolean,
    val message: String,
    )


@Serializable
enum class ActorType{
  GUARANTOR
}
