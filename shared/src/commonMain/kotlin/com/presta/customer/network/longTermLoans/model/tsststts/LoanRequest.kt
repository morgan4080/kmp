package com.presta.customer.network.longTermLoans.model.tsststts

import kotlinx.serialization.Serializable

@Serializable
data class LoanRequest(
    val loanDate: String,
    val refId: String,
    val amount: Int,
    val loanNumber: String,
)

