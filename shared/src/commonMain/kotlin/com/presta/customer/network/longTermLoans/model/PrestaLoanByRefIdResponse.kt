package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaLoanByRefIdResponse(
    val refId:  String,
    val loanDate: String,
    val loanRequestNumber:String,
    val loanProductName:String,
    val loanProductRefId: String,
    val loanAmount: Double,
    val memberRefId: String,
    val memberFirstName: String,
    val memberLastName: String
)

