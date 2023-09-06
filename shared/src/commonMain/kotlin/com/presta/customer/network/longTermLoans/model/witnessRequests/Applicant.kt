package com.presta.customer.network.longTermLoans.model.witnessRequests

import kotlinx.serialization.Serializable

@Serializable
data class Applicant(
    val refId: String,
    val firstName: String,
    val lastName: String,
    val memberNo: String,
)