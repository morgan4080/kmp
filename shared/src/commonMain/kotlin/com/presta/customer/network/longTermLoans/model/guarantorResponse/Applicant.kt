package com.presta.customer.network.longTermLoans.model.guarantorResponse

import kotlinx.serialization.Serializable

@Serializable
data class Applicant(
    val refId: String,
    val firstName: String,
    val lastName: String,
)
