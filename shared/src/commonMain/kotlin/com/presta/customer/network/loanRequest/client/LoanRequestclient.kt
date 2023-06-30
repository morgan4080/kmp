package com.presta.customer.network.loanRequest.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.loanRequest.errorHandler.loanRequestErrorHandler
import com.presta.customer.network.loanRequest.model.AllBanksContent
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
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


@Serializable
data class LoanRequestData @OptIn(ExperimentalSerializationApi::class) constructor(
    val amount: Int,
    val currentTerm: Boolean,
    val customerRefId: String,
    val disbursementAccountReference: String,
    val disbursementMethod: DisbursementMethod,
    val loanPeriod: Int,
    @EncodeDefault val loanType: LoanType,
    val productRefId: String,
    val referencedLoanRefId: String?,
    val requestId: String? = null,
    val sessionId: String,
)

@Serializable
data class CustomerBank(
    val accountNumber: String,
    val accountName: String,
    val paybillName: String,
    val paybillNumber: String,
    val userRefId: String
)

class PrestaLoanRequestClient(
    private val httpClient: HttpClient
) {
    suspend fun sendLoanRequest(
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
        requestId: String? = null,
        sessionId: String
    ): LoanRequestResponse {
        return loanRequestErrorHandler {
            httpClient.post(NetworkConstants.PrestaLoanRequest.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    LoanRequestData(
                        amount = amount,
                        currentTerm = currentTerm,
                        customerRefId = customerRefId,
                        disbursementAccountReference = disbursementAccountReference,
                        disbursementMethod = disbursementMethod,
                        loanPeriod = loanPeriod,
                        loanType = loanType,
                        productRefId = productRefId,
                        referencedLoanRefId = referencedLoanRefId,
                        requestId = requestId,
                        sessionId = sessionId
                    )
                )
            }
        }
    }
    suspend fun pollLoanApplicationStatus(
        token: String,
        requestId: String
    ): PrestaLoanPollingResponse {
        return loanRequestErrorHandler {
            httpClient.get("${NetworkConstants.PrestaLoanRequest.route}/${requestId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }
    suspend fun sendLoanQuotationRequest(
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
        requestId: String? = null,
        sessionId: String
    ): LoanQuotationResponse {
        val payload = LoanRequestData(
            amount = amount,
            currentTerm = currentTerm,
            customerRefId = customerRefId,
            disbursementAccountReference = disbursementAccountReference,
            disbursementMethod = disbursementMethod,
            loanPeriod = loanPeriod,
            loanType = loanType,
            productRefId = productRefId,
            referencedLoanRefId = referencedLoanRefId,
            requestId = requestId,
            sessionId = sessionId
        )
        println("::::Quotation Payload::::::")
        println(payload)
        return loanRequestErrorHandler {
            httpClient.post(NetworkConstants.PrestaLoanQuotation.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    payload
                )
            }
        }
    }
    suspend fun getLoansByApplicationStatus(
        token: String,
        customerRefId: String,
        applicationStatus: List<LoanApplicationStatus>
    ): List<PrestaLoanApplicationStatusResponse> {
        return loanRequestErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetPendingApplications.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("customerRefId", customerRefId)
                    encodedParameters.append("applicationStatus", applicationStatus.joinToString(separator = ","))
                }
            }
        }
    }
    suspend fun getAllBanks(
        token: String
    ): AllBanksContent {
        return loanRequestErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetBanks.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("startLetter", "A")
                    parameters.append("endLetter", "Z")
                    parameters.append("start", "0")
                    parameters.append("limit", "100")
                }
            }
        }
    }
    suspend fun getCustomerBanks(
        token: String,
        customerRefId: String
    ): List<PrestaCustomerBanksResponse> {
        return loanRequestErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetBankAccounts.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("customerRefId", customerRefId)
                }
            }
        }
    }
    suspend fun createCustomerBanks(
        token: String,
        customerRefId: String,
        accountNumber: String,
        accountName: String,
        paybillName: String,
        paybillNumber: String
    ): PrestaCustomerBankCreatedResponse {
        val payload = CustomerBank(
            accountNumber = accountNumber,
            accountName = accountName,
            paybillName = paybillName,
            paybillNumber = paybillNumber,
            userRefId = customerRefId,
        )
        return loanRequestErrorHandler {
            httpClient.post(NetworkConstants.PrestaGetBankAccounts.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    payload
                )
                url {
                    parameters.append("customerRefId", customerRefId)
                }
            }
        }
    }
    suspend fun deleteCustomerBank(
        token: String,
        bankAccountRefId: String
    ): PrestaCustomerBankDeletedResponse {
        return loanRequestErrorHandler {
            httpClient.delete(NetworkConstants.PrestaGetBankAccounts.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("bankAccountRefId", bankAccountRefId)
                }
            }
        }
    }
}