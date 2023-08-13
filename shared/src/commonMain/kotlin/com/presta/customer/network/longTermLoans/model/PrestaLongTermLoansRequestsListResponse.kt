package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaLongTermLoansRequestsListResponse(
    val refId: String,
    val loanDate: String,
    val loanRequestNumber: String,
    val loanProductName: String,
    val loanProductRefId: String,
    val loanAmount: Double,
    val status: String,
    val signingStatus: String,
    val acceptanceStatus: String,
    val applicationStatus: String,
    val memberRefId: String,
    val isActive: Boolean,
    val memberNumber: String,
    val memberFirstName: String,
    val memberLastName: String,
    val phoneNumber: String,
    val loanRequestProgress: Double,
)
