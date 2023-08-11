package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaLoanRequestByRequestRefId(
    val acceptanceStatus: String,
    val applicantSigned: Boolean,
    val applicationStatus: String,
    val isActive: Boolean,
    val loanAmount: Double,
    val loanDate: String,
    val loanProductName: String,
    val loanProductRefId: String,
    val loanRequestNumber: String,
    val loanRequestProgress: Double,
    val memberFirstName: String,
    val memberLastName: String,
    val memberNumber: String,
    val memberRefId: String,
    val pdfThumbNail: String,
    val pendingReason: String,
    val phoneNumber: String,
    val refId: String,
    val signingStatus: String,
    val status: String,
    val totalDeposits: Double,
    val witnessAccepted: Boolean,
    val witnessMemberNo: String,
    val witnessName: String,
    val witnessRefId: String,
    val witnessSigned: Boolean,
    val zohoDocumentId: String,
    val zohoRequest: String,
    val zohoRequestId: String
)

