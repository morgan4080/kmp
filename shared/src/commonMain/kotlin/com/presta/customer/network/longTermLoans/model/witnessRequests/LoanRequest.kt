package com.presta.customer.network.longTermLoans.model.witnessRequests

import kotlinx.serialization.Serializable

@Serializable
data class LoanRequest(
    val amount: Double,
    val loanDate: String,
    val loanNumber: String,
    val refId: String
)