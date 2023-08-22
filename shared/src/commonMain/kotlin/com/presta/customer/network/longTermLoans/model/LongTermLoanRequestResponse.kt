package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class LongTermLoanRequestResponse constructor(
    val refId: String,
    val loanDate: String,
    val loanRequestNumber: String,
    val loanProductName: String,
    val loanProductRefId: String,
    val loanAmount: Double,
    val memberRefId: String,
    val isActive: Boolean,
    val memberNumber: String,
    val memberFirstName: String,
    val memberLastName: String,
    val phoneNumber: String,
    val loanRequestProgress: Double,
    val totalDeposits: Double,
    val applicantSigned: Boolean,
    val witnessAccepted: Boolean,
    val witnessSigned: Boolean,
    val pdfThumbNail: String,
    val guarantorList: List<GuarantorDataResponse>,
)
