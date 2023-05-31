package com.presta.customer.network.profile.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.profile.errorHandler.profileErrorHandler
import com.presta.customer.network.profile.model.PrestaBalancesResponse
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath

class PrestaProfileClient(
    private val httpClient: HttpClient
) {
    suspend fun getUserSavingsData (
        token: String,
        memberRefId: String,
    ): PrestaBalancesResponse {

        return profileErrorHandler {
            httpClient.get("${NetworkConstants.PrestaGetSavingsBalance.route}/${memberRefId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }


    suspend fun getUserTransactionHistoryData (
        token: String,
        memberRefId: String,
        purposeIds: List<String>
    ): List<PrestaTransactionHistoryResponse> {
        return profileErrorHandler {
            httpClient.get("${NetworkConstants.PrestaGetTransactionsHistory.route}/${memberRefId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    encodedParameters.append("purposeIds", purposeIds.joinToString(separator = ","))
                }
            }
        }
    }


    suspend fun getTransactionsMappingData (
        token: String
    ): Map<String, String> {
        return profileErrorHandler {
            httpClient.get("${NetworkConstants.PrestaGetTransactionsHistory.route}/mapping") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }

}

