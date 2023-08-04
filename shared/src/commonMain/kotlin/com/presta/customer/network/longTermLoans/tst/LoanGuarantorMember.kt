package com.presta.customer.network.longTermLoans.tst

data class LoanGuarantorMember(
    val committedAmount: Int,
    val loanNumber: String,
    val loanRefId: String,
    val memberNumber: String,
    val memberRefId: String
)