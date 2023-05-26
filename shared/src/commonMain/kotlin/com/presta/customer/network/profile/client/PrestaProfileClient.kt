package com.presta.customer.network.profile.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.profile.errorHandler.profileErrorHandler
import com.presta.customer.network.profile.model.PrestaBalancesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class PrestaProfileClient(
    private val httpClient: HttpClient
) {
    suspend fun getUserSavingsData(
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
}

