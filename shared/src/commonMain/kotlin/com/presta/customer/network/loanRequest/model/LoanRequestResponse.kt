package com.presta.customer.network.loanRequest.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class LoanRequestResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val amount: Int? = null,
    @EncodeDefault val currentTerm: String? = null,
    @EncodeDefault val customerRefId: String? = null,
    @EncodeDefault val disbursementAccountReference: String? = null,
    @EncodeDefault val disbursementMethod: DisbursementMethod? = null,
    @EncodeDefault val loanPeriod: Int? = null,
    @EncodeDefault val loanType: LoanType? = null,
    @EncodeDefault val productRefId: String? = null,
    @EncodeDefault val referencedLoanRefId: String? = null,
    @EncodeDefault val requestId: String? = null,
    @EncodeDefault val sessionId: String? = null
)

@Serializable
enum class DisbursementMethod {
    MOBILEMONEY,
    BANK
}

//[_NORMAL_LOAN, _TOP_UP]

@Serializable
enum class LoanType {
    _NORMAL_LOAN,
    _TOP_UP
}