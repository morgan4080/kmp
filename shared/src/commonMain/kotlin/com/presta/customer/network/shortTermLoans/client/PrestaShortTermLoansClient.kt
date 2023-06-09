package com.presta.customer.network.shortTermLoans.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.profile.errorHandler.profileErrorHandler
import com.presta.customer.network.profile.model.PrestaSavingsBalancesResponse
import com.presta.customer.network.shortTermLoans.errorHandler.shortTermLoansErrorHandler
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermTopUpListResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class PrestaShortTermLoansClient(
    private val httpClient: HttpClient
) {
    suspend fun getShortTermProductsList(
        token: String,
        memberRefId: String,
    ): List<PrestaShortTermProductsListResponse> {
        return shortTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetTShortTermProductsList.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("customerRefId", memberRefId)
                }
            }
        }
    }

    //Get  Loan By Id
    suspend fun getShortTermProductLoanById(
        token: String,
        loanId: String,
    ): PrestaShortTermProductsListResponse {
        return profileErrorHandler {
            httpClient.get("${NetworkConstants.PrestaGetTShortTermProductsList.route}/${loanId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun getShortTermTopUpList(
        token: String,
        memberRefId: String,
        session_id: String
    ): PrestaShortTermTopUpListResponse {
        return shortTermLoansErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetShortTermToUpList.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("sessionId", session_id)
                    parameters.append("customerRefId", memberRefId)
                }
            }
        }
    }
}