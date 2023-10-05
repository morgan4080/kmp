package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class LongTermLoanRequestResponse @OptIn(ExperimentalSerializationApi::class) constructor(
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
    @EncodeDefault val pendingReason: String? = null,
    @EncodeDefault val pendingReasonClass: PendingReasons? = null,
)

enum class PendingReasons {
    MISSING_GUARANTOR_EMAIL,
    MISSING_APPLICANT_EMAIL,
    MISSING_WITNESS_EMAIL,
    MISSING_DOCUMENT_REQUIRED_FIELD,
    ZOHO_SIGN_ERROR,
    INELIGIBLE_GUARANTOR,
    GUARANTOR_REQUIRED,
    GUARANTOR_COMMITTED_AMOUNT_ZERO,
    GUARANTOR_ELIGIBILITY,
    CORE_BANKING_POST_FAILED,
    INTERNAL_ERROR,
}


