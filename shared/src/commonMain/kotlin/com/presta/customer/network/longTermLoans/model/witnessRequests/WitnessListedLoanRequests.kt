package com.presta.customer.network.longTermLoans.model.witnessRequests

import kotlinx.serialization.Serializable

@Serializable
data class WitnessListedLoanRequests(
    val refId: String,
    val loanNumber: String,
    val amount: Double,
)
