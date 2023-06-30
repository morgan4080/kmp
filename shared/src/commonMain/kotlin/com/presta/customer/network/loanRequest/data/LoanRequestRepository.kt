package com.presta.customer.network.loanRequest.data

import com.presta.customer.network.loanRequest.model.DisbursementMethod
import com.presta.customer.network.loanRequest.model.LoanApplicationStatus
import com.presta.customer.network.loanRequest.model.LoanQuotationResponse
import com.presta.customer.network.loanRequest.model.LoanRequestResponse
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.network.loanRequest.model.PrestaBanksResponse
import com.presta.customer.network.loanRequest.model.PrestaCustomerBankCreatedResponse
import com.presta.customer.network.loanRequest.model.PrestaCustomerBankDeletedResponse
import com.presta.customer.network.loanRequest.model.PrestaCustomerBanksResponse
import com.presta.customer.network.loanRequest.model.PrestaLoanApplicationStatusResponse
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
    ): Result<LoanRequestResponse>

    suspend fun pollLoanApplicationStatus(
        token: String,
        requestId: String
    ): Result<PrestaLoanPollingResponse>


    //loan  Quotation
    suspend fun loanQuotationRequest(
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
    ): Result<LoanQuotationResponse>

    suspend fun getPendingLoans(
        token: String,
        customerRefId: String,
        applicationStatus: List<LoanApplicationStatus>
    ): Result<List<PrestaLoanApplicationStatusResponse>>

    suspend fun getAllBanks(
        token: String,
    ): Result<List<PrestaBanksResponse>>

    suspend fun getCustomerBanks(token: String, customerRefId: String): Result<List<PrestaCustomerBanksResponse>>

    suspend fun createCustomerBanks(
        token: String,
        customerRefId: String,
        accountNumber: String,
        accountName: String,
        paybillName: String,
        paybillNumber: String
    ): Result<PrestaCustomerBankCreatedResponse>
    suspend fun deleteCustomerBank(
        token: String,
        bankAccountRefId: String
    ): Result<PrestaCustomerBankDeletedResponse>
}