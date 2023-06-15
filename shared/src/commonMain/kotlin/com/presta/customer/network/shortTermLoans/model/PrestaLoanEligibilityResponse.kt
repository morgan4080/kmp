package com.presta.customer.network.shortTermLoans.model

import com.presta.customer.network.loanRequest.model.LoanType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaLoanEligibilityResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val isEligible: Boolean,
    val isEligibleForNormalLoanAndTopup: Boolean,
    @EncodeDefault val loanType: LoanType,
    val userBalance: Double,
    val amountAvailable: Double
)