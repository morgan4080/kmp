package com.presta.customer.network.profile.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.profile.errorHandler.ProfileErrorHandler
import com.presta.customer.network.profile.model.PrestaBalancesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class PrestaProfileClient(
    private val httpClient: HttpClient) {
    suspend fun getUserSavingsData(
        token: String,
        memberIdentifier: String,
    ): PrestaBalancesResponse {

        return ProfileErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetSavingsBalance.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("memberIdentifier", memberIdentifier)
                    parameters.append("force", "true")

                }
            }
        }
    }
}

