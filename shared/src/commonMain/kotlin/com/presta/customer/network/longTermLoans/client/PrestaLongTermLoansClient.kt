package com.presta.customer.network.longTermLoans.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.loanRequest.errorHandler.loanRequestErrorHandler
import com.presta.customer.network.longTermLoans.errorHandler.longTermLoansErrorHandler
import com.presta.customer.network.longTermLoans.model.ActorType
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.longTermLoans.model.Guarantor
import com.presta.customer.network.longTermLoans.model.LongTermLoanRequestResponse
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLoanByRefIdResponse
import com.presta.customer.network.longTermLoans.model.PrestaLoanRequestByRequestRefId
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategoriesChildren
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansRequestsListResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import com.presta.customer.network.longTermLoans.model.PrestaZohoSignUrlResponse
import com.presta.customer.network.longTermLoans.model.guarantorResponse.PrestaGuarantorResponse
import com.presta.customer.network.longTermLoans.model.PrestaGuarantorAcceptanceResponse
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
data class ZohoSignUrlPayload(
    val loanRequestRefId: String,
    val actorRefId: String,
    val actorType: ActorType
)
@Serializable
data class GuarantorPayLoad(
    val memberRefId: String,
    val committedAmount: String,
    val guarantorName: String
)
@Serializable
data class GuarantorListPayLoad(
    val guarantorList: ArrayList<GuarantorPayLoad>,
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
    ): List<PrestaGuarantorResponse> {
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

    suspend fun getLoanRequestByRefId(
        token: String,
        loanRequestRefId: String,
    ): PrestaLoanByRefIdResponse {
        return longTermLoansErrorHandler {
            httpClient.get("${NetworkConstants.PrestaLongTermLoanRequestByRefId.route}/${loanRequestRefId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun sendGuarantorAcceptanceStatus(
        token: String,
        guarantorshipRequestRefId: String,
        isAccepted: Boolean
    ): PrestaGuarantorAcceptanceResponse {
        return loanRequestErrorHandler {
            httpClient.post("${NetworkConstants.PrestaGetGuarantorshipRequests.route}/${guarantorshipRequestRefId}/${isAccepted}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun getLoanByLoanRequestRefId(
        token: String,
        loanRequestRefId: String,
    ): PrestaLoanRequestByRequestRefId {
        return longTermLoansErrorHandler {
            httpClient.get("${NetworkConstants.PrestaGetLoanByLoanRequestId.route}/${loanRequestRefId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun sendZohoSignUrlPayload(
        token: String,
        loanRequestRefId: String,
        actorRefId: String,
        actorType: ActorType
    ): PrestaZohoSignUrlResponse {
        return loanRequestErrorHandler {
            httpClient.post(NetworkConstants.PrestaGetZohoSignUrl.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    ZohoSignUrlPayload(
                        loanRequestRefId = loanRequestRefId,
                        actorRefId = actorRefId,
                        actorType = actorType
                    )
                )
            }
        }
    }

    suspend fun getLongTermLoanRequestsList(
        token: String,
        memberRefId: String,
    ): PrestaLongTermLoansRequestsListResponse {
        return longTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaLongTermLoansRequetsList.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    encodedParameters.append("memberRefId", memberRefId)
                }
            }
        }
    }

    suspend fun getLongTermLoanRequestsFilteredList(
        token: String,
        memberRefId: String,
    ): PrestaLongTermLoansRequestsListResponse {
        return longTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaLongTermLoansRequetsList.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    encodedParameters.append("memberRefId", memberRefId)
                    encodedParameters.append("order", "ASC")
                    encodedParameters.append("pageSize", "2")
                }
            }
        }
    }
    suspend fun upDateGuarantor(
        token: String,
//        loanRequestRefId: String,
//        guarantorRefId: String,
        guarantorList: ArrayList<GuarantorPayLoad>,
    ): LongTermLoanRequestResponse {
        return loanRequestErrorHandler {
            httpClient.post(NetworkConstants.PrestaLongTermLoanRequest.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    GuarantorListPayLoad(
                        guarantorList = guarantorList,
                    )
                )
            }
        }
    }
}