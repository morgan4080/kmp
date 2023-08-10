package com.presta.customer.network.longTermLoans.model.tsststts

import kotlinx.serialization.Serializable

@Serializable
data class Applicant(
    val firstName: String,
    val lastName: String,
    val memberNo: String,
    val refId: String
)