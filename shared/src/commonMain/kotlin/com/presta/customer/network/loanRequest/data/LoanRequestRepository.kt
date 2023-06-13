package com.presta.customer.network.loanRequest.data

import com.presta.customer.network.loanRequest.model.DisbursementMethod
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.network.loanRequest.model.PrestaLoanPollingResponse

interface LoanRequestRepository {
    suspend fun requestLoan(
        token: String,
        amount: Int,
        currentTerm: Boolean,
        customerRefId: String,
        disbursementAccountReference: String,
        disbursementMethod: DisbursementMethod,
        loanPeriod: Int,
        loanType: LoanType,
        productRefId: String,
        referencedLoanRefId: String?,
        requestId: String?,
        sessionId: String
    ): Result<String>

    suspend fun pollLoanApplicationStatus(
        token: String,
        requestId: String
    ): Result<PrestaLoanPollingResponse>

}