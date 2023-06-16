package com.presta.customer.network.shortTermLoans.model

import com.presta.customer.network.loanRequest.model.LoanType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

enum class EligibilityReasons {
    UNKNOWNUSER,
    NOTACTIVATEDFORLOAN,
    EXISTINGLOAN,
    TOPUP_LOAN_NOT_DEFINED,
    APPRAISALFAILED,
    NOFUNDS, APPROVALLIMITNULL, LOANINPROGRESS, INVALID_PHONE_NUMBER,
    LIMIT_REACHED, INACTIVE_ACCOUNT, ACCOUNT_KYC_NOT_VALIDATED, FUNDS_LESS_THAN_REQUESTED,
    CUSTOMER_KYC_DOCUMENTS_INCOMPLETE, CUSTOMER_EMAIL_INCOMPLETE, GUARANTOR_EMAIL_INCOMPLETE,
    PRODUCT_UNAVAILABLE, EXISTINGLOAN_NO_TOPUP_ELIGIBLE, PRODUCT_UNDEFINED
}
@Serializable
data class PrestaLoanEligibilityResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val isEligible: Boolean,
    val isEligibleForNormalLoanAndTopup: Boolean,
    @EncodeDefault val reason: EligibilityReasons? = null,
    val loanType: LoanType,
    @EncodeDefault val userBalance: Double = 0.0,
    @EncodeDefault val amountAvailable: Double = 0.0,
    @EncodeDefault val description: String = ""
)

// {"isEligible":false,"isEligibleForNormalLoanAndTopup":false,"reason":"LOANINPROGRESS","loanType":"_NORMAL_LOAN","description":"You have a loan in progress"}

//Dennis

//Dennis update