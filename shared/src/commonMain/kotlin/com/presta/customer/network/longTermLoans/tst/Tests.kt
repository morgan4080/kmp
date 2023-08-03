package com.presta.customer.network.longTermLoans.tst

data class Tests(
    //val details: Details,
    val guarantorList: List<Guarantor>,
    val loanAmount: Int,
    val loanProductRefId: String,
    val memberRefId: String,
    val witnessRefId: String
)