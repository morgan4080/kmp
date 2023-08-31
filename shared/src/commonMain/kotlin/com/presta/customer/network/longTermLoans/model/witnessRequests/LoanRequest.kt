package com.presta.customer.network.longTermLoans.model.witnessRequests

import kotlinx.serialization.Serializable

@Serializable
data class LoanRequest(
    val loanDate: String,
    val refId: String,
    val loanNumber: String,
    val amount: Double,
)
