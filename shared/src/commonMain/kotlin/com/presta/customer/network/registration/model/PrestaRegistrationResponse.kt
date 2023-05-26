package com.presta.customer.network.registration.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/*
    {"creditData":{"isApprovedForLoan":false,"canIntroduceBeneficiary":false,"approvedLimit":0.0,"currentBalance":0.0,"sharesBalance":0.0,"depositBalance":0.0,"membershipBalance":0.0,"dividendsBalance":0.0},"appraisalStatus":"PENDING"}*/

@Serializable
data class PrestaRegistrationResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val uuid: String,
    val refId: String,
    val fullName: String,
    val phoneNumber: String,
    @EncodeDefault val email: String? = null,
    val idNumber: String,
    @EncodeDefault val memberNo: String? = null,
    val creditData: CreditData,
    @EncodeDefault val loanSummary: LoanSummary? = null,
    @EncodeDefault val customerMetadata: CustomerMetadata? = null,
    @EncodeDefault val appraisalStatus: ApprovalStatus? = null
)

@Serializable
data class CreditData @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val isApprovedForLoan: Boolean = false,
    @EncodeDefault val canIntroduceBeneficiary: Boolean = false,
    @EncodeDefault val approvedLimit: Double = 0.0,
    @EncodeDefault val currentBalance: Double = 0.0,
    @EncodeDefault val sharesBalance: Double = 0.0,
    @EncodeDefault val depositBalance: Double = 0.0,
    @EncodeDefault val membershipBalance: Double = 0.0,
    @EncodeDefault val dividendsBalance: Double = 0.0,
    @EncodeDefault val approvedForLoan: Boolean  = false
)
@Serializable
data class LoanSummary(
    val activeLoanCount: Double,
    val inprogressLoanCount: Double
)
@Serializable
data class CustomerMetadata(
    val branchRefId: String,
    val branch: String,
    val regionRefId: String,
    val region: String,
    val relationshipManagerRefId: String,
    val relationshipManager: String
)
@Serializable
enum class ApprovalStatus {
    PENDING, APPROVED, DENIED, FAILED
}