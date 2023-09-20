package com.presta.customer.network.longTermLoans.model.guarantorResponse

import kotlinx.serialization.Serializable

@Serializable
data class LoanRequest(
    val loanDate: String,
    val refId: String,
    val loanNumber: String,
    val amount: Double,
)

