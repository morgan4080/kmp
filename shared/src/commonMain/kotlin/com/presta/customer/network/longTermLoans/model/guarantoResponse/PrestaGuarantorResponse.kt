package com.presta.customer.network.longTermLoans.model.guarantoResponse

import kotlinx.serialization.Serializable

@Serializable
data class PrestaGuarantorResponse(
    val refId: String,
    val memberNumber: String,
    val memberRefId: String,
    val firstName: String,
    val lastName: String,
    val eligible: Boolean,
    val eligibilityMessage: String,
    val isAccepted: Boolean,
    val isSigned: Boolean,
    val isActive: Boolean,
    val committedAmount: Double,
    val applicant: Applicant,
    val loanRequest: LoanRequest,
)
