package com.presta.customer.network.longTermLoans.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.loanRequest.errorHandler.loanRequestErrorHandler
import com.presta.customer.network.longTermLoans.errorHandler.longTermLoansErrorHandler
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.longTermLoans.model.Guarantor
import com.presta.customer.network.longTermLoans.model.LongTermLoanRequestResponse
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategoriesChildren
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import com.presta.customer.network.longTermLoans.model.tst.TestguarantorItem
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable


@Serializable
data class GuarantorDetails(
    val value: String,
    val type: String
)

@Serializable
data class DetailsData(
    val loan_purpose_1: String,
    val loan_purpose_2: String,
    val loan_purpose_3: String,
    val loanPurposeCode: String,
    val loanPeriod: String,
    val repayment_period: String,
    val employer_name: String,
    val employment_type: String,
    val employment_number: String,
    val business_location: String,
    val business_type: String,
    val net_salary: String,
    val gross_salary: String,
    val disbursement_mode: String,
    val repayment_mode: String,
    val loan_type: String,
    val kraPin: String
)

@Serializable
data class LongTermLoanRequestData constructor(
    val details: DetailsData,
    val loanProductName: String,
    val loanProductRefId: String,
    val selfCommitment: Double,
    val loanAmount: Double,
    val memberRefId: String,
    val memberNumber: String,
    val witnessRefId: String?,
    val guarantorList: ArrayList<Guarantor>,
)
class PrestaLongTermLoansClient(
    private val httpClient: HttpClient
) {
    suspend fun getLongTermLoansData(
        token: String,
    ): PrestaLongTermLoansProductResponse {
        return longTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetLongTermLoansProducts.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    encodedParameters.append("order", "ASC")
                    encodedParameters.append("pagesize", "10")
                    encodedParameters.append("includeInActive", "false")
                }
            }
        }
    }

    suspend fun getLongTermProductLoanById(
        token: String,
        loanRefId: String,
    ): LongTermLoanResponse {
        return longTermLoansErrorHandler {
            httpClient.get("${NetworkConstants.PrestaGetLongTermLoansProducts.route}/${loanRefId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun getLoanCategories(
        token: String,
    ): List<PrestaLongTermLoanCategoriesResponse> {
        return longTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetLoanCategories.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun getLoanSubCategories(
        token: String,
        parent: String
    ): List<PrestaLongTermLoanSubCategories> {
        return longTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetLoanCategories.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("parent", parent)
                }
            }
        }
    }

    suspend fun getLoanSubCategoriesChildren(
        token: String,
        parent: String,
        child: String
    ): List<PrestaLongTermLoanSubCategoriesChildren> {
        return longTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetLoanCategories.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("parent", parent)
                    parameters.append("child", child)
                }
            }
        }
    }

    suspend fun getClientSettings(
        token: String,
    ): ClientSettingsResponse {
        return longTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetClientSettings.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }
    suspend fun sendLongTermLoanRequest(
        token: String,
        details: DetailsData,
        loanProductName: String,
        loanProductRefId: String,
        selfCommitment: Double,
        loanAmount: Double,
        memberRefId: String,
        memberNumber: String,
        witnessRefId: String?,
        guarantorList: ArrayList<Guarantor>,
    ): LongTermLoanRequestResponse {
        return loanRequestErrorHandler {
            httpClient.post(NetworkConstants.PrestaLongTermLoanRequest.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    LongTermLoanRequestData(
                        details = details,
                        loanProductName = loanProductName,
                        loanProductRefId = loanProductRefId,
                        selfCommitment = selfCommitment,
                        loanAmount = loanAmount,
                        memberRefId = memberRefId,
                        memberNumber = memberNumber,
                        witnessRefId = witnessRefId,
                        guarantorList = guarantorList,
                    )
                )
            }
        }
    }
    suspend fun getGuarantorshipRequests(
        token: String,
        memberRefId: String,
    ):List<TestguarantorItem>{
        return longTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetGuarantorshipRequests.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    encodedParameters.append("acceptanceStatus", "ANY")
                    encodedParameters.append("memberRefId", memberRefId)
                }
            }
        }
    }
}