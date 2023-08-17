package com.presta.customer.network.longTermLoans.model.favouriteGuarantor

data class PrestaFavouriteGuarantorResponse(
    val firstName: String,
    val lastName: String,
    val memberNumber: String,
    val memberRefId: String,
    val refId: String
)