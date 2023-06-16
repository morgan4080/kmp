package com.presta.customer.network.loanRequest.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable



enum class LoanApplicationStatus{
    NEWAPPLICATION, INITIATED, INPROGRESS, COMPLETED, FAILED
}

enum class AccountingStatuses {
    NOTACCOUNTED, ACCOUNTED
}
enum class DisbursementStatus {
    INITIATED, INPROGRESS, DISBURSED, NOTDISBURSED, FAILED
}
enum class AppraisalStatus {
    PENDING, APPROVED, DENIED, FAILED
}
@Serializable
data class AccountingResult @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val accountingStatus: AccountingStatuses? = null,
)
@Serializable
data class DisbursementResult @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val disbursementStatus: DisbursementStatus? = null,
    val transactionId: String
)
@Serializable
data class AppraisalResult @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val appraisalStatus: AppraisalStatus? = null,
)

@Serializable
data class PrestaLoanPollingResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val applicationStatus: LoanApplicationStatus? = null,
    val appraisalResult: AppraisalResult,
    @EncodeDefault val disbursementResult: DisbursementResult? = null,
    @EncodeDefault val accountingResult: AccountingResult? = null,
)

@Serializable
enum class DisbursementMethod {
    MOBILEMONEY,
    BANK
}


@Serializable
enum class LoanType {
    _NORMAL_LOAN,
    _TOP_UP
}

@Serializable
data class LoanRequestResponse (val requestId: String)