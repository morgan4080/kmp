package com.presta.customer.network.longTermLoans.model.witnessRequests

import kotlinx.serialization.Serializable

@Serializable
data class PrestaWitnessRequestResponse(
    val applicant: Applicant,
    val firstName: String,
    val lastName: String,
    val loanDate: String,
    val loanRequest: LoanRequest,
    val memberRefId: String,
    val witnessAcceptanceStatus: String,
    val witnessAccepted: Boolean,
    val witnessSigned: Boolean
)