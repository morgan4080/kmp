package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaLoanRequestByRequestRefId @OptIn(ExperimentalSerializationApi::class) constructor(
    val refId: String,
    val loanDate: String,
    val loanRequestNumber: String,
    val loanProductName: String,
    val loanProductRefId: String,
    val loanAmount: Double,
    val applicationStatus: String,
    val memberRefId: String,
    val isActive: Boolean,
    val memberNumber: String,
    val memberFirstName: String,
    val memberLastName: String,
    val phoneNumber: String,
    val loanRequestProgress: Double,
    val totalDeposits: Double,
    @EncodeDefault val applicantSigned: Boolean?=null,
    @EncodeDefault val witnessAccepted: Boolean?=null,
    @EncodeDefault val witnessSigned: Boolean?=null,
    @EncodeDefault val pdfThumbNail: String? = null,
    val guarantorList: List<GuarantorDataResponse>,
    //val acceptanceStatus: String,
    //val applicationStatus: String,
    //  val pendingReason: String,
    //val signingStatus: String,
    //val status: String,
    //val witnessMemberNo: String,
    // val witnessName: String,
    //val witnessRefId: String,
    //val zohoDocumentId: String,
    //val zohoRequest: String,
    //val zohoRequestId: String,
)


