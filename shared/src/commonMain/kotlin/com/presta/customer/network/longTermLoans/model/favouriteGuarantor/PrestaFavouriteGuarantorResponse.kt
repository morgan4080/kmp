package com.presta.customer.network.longTermLoans.model.favouriteGuarantor

import kotlinx.serialization.Serializable

@Serializable
data class PrestaFavouriteGuarantorResponse(
    val refId: String,
    val memberRefId: String,
    val firstName: String,
    val lastName: String,
    val memberNumber: String,
)
