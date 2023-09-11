package com.presta.customer.network.signHome.model

import kotlinx.serialization.Serializable

@Serializable
data class LoanGuarantorMember(
    val committedAmount: Int,
    val loanNumber: String,
    val loanRefId: String,
    val memberNumber: String,
    val memberRefId: String
)