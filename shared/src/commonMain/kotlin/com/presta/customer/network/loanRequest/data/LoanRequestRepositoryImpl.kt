package com.presta.customer.network.loanRequest.data

import com.presta.customer.network.loanRequest.client.PrestaLoanRequestClient
import com.presta.customer.network.loanRequest.model.DisbursementMethod
import com.presta.customer.network.loanRequest.model.LoanRequestResponse
import com.presta.customer.network.loanRequest.model.LoanType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
class LoanRequestRepositoryImpl : LoanRequestRepository,KoinComponent {
    private val loanRequestClient by inject<PrestaLoanRequestClient>()
    override suspend fun requestLoan(
        token: String,
        amount: Int,
        currentTerm: String,
        customerRefId: String,
        disbursementAccountReference: String,
        disbursementMethod: DisbursementMethod,
        loanPeriod: Int,
        loanType:LoanType,
        productRefId: String,
        referencedLoanRefId: String?,
        requestId: String?,
        sessionId: String
    ): Result<LoanRequestResponse> {
        return try {
            val response = loanRequestClient.sendLoanRequest(
                token = token,
                amount = amount,
                currentTerm = currentTerm,
                customerRefId = customerRefId,
                disbursementAccountReference = disbursementAccountReference,
                disbursementMethod=disbursementMethod,
                loanPeriod = loanPeriod,
                loanType = loanType,
                productRefId = productRefId,
                referencedLoanRefId = referencedLoanRefId,
                requestId = requestId,
                sessionId = sessionId
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}