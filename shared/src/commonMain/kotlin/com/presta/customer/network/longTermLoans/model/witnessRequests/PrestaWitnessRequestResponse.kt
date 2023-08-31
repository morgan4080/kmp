package com.presta.customer.network.longTermLoans.model.witnessRequests

import kotlinx.serialization.Serializable

@Serializable
data class PrestaWitnessRequestResponse(
    val loanDate: String,
    val memberRefId: String,
    val firstName: String,
    val lastName: String,
    val witnessAcceptanceStatus: String,
    val witnessSigned: Boolean,
    val witnessAccepted: Boolean,
    val applicant: Applicant,
    val loanRequest: LoanRequest,
)

