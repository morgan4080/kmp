package com.presta.customer.network.loanRequest.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable



enum class LoanRequestStatus{
    NEWAPPLICATION, INITIATED, INPROGRESS, COMPLETED, FAILED
}

@Serializable
data class PrestaLoanPollingResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val applicationStatus: LoanRequestStatus? = null
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