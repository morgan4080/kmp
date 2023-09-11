package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class LoanRequestListData @OptIn(ExperimentalSerializationApi::class) constructor(
    val refId: String,
    val loanDate: String,
    val loanRequestNumber: String,
    val loanProductName: String,
    val loanProductRefId: String,
    val loanAmount: Double,
    val status: String,
    @EncodeDefault val signingStatus: String?=null,
    val acceptanceStatus: String,
    val applicationStatus: LoanApplicationStatus,
    val memberRefId: String,
    val isActive: Boolean,
    val memberNumber: String,
    val memberFirstName: String,
    val memberLastName: String,
    val phoneNumber: String,
    val loanRequestProgress: Double,
    @EncodeDefault val applicantSigned: Boolean?=null
)
