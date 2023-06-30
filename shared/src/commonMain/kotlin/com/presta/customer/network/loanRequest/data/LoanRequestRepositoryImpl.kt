package com.presta.customer.network.loanRequest.data

import com.presta.customer.network.loanRequest.client.PrestaLoanRequestClient
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoanRequestRepositoryImpl : LoanRequestRepository,KoinComponent {
    private val loanRequestClient by inject<PrestaLoanRequestClient>()
    override suspend fun requestLoan(
        token: String,
        amount: Int,
        currentTerm: Boolean,
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

    override suspend fun pollLoanApplicationStatus(
        token: String,
        requestId: String
    ): Result<PrestaLoanPollingResponse> {
        return try {
            val response = loanRequestClient.pollLoanApplicationStatus(token, requestId)
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun loanQuotationRequest(
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
    ): Result<LoanQuotationResponse> {

        return try {
            val response = loanRequestClient.sendLoanQuotationRequest(
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

    override suspend fun getPendingLoans(
        token: String,
        customerRefId: String,
        applicationStatus: List<LoanApplicationStatus>
    ): Result<List<PrestaLoanApplicationStatusResponse>> {
        return try {
            val response = loanRequestClient.getLoansByApplicationStatus(
                token,
                customerRefId,
                applicationStatus
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getAllBanks(token: String): Result<List<PrestaBanksResponse>> {
        return try {
            val response = loanRequestClient.getAllBanks(
                token
            )
            Result.success(response.content)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getCustomerBanks(token: String, customerRefId: String): Result<List<PrestaCustomerBanksResponse>> {
        return try {
            val response = loanRequestClient.getCustomerBanks(
                token,
                customerRefId
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun createCustomerBanks(
        token: String,
        customerRefId: String,
        accountNumber: String,
        accountName: String,
        paybillName: String,
        paybillNumber: String
    ): Result<PrestaCustomerBankCreatedResponse> {
        return try {
            val response = loanRequestClient.createCustomerBanks(
                token,
                customerRefId,
                accountNumber,
                accountName,
                paybillName,
                paybillNumber
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun deleteCustomerBank(
        token: String,
        bankAccountRefId: String
    ): Result<PrestaCustomerBankDeletedResponse> {
        return try {
            val response = loanRequestClient.deleteCustomerBank(
                token,
                bankAccountRefId
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}