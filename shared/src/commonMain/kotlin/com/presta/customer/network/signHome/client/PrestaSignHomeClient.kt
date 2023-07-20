package com.presta.customer.network.signHome.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.signHome.errorHandler.signHomeErrorHandler
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class PrestaSignHomeClient(
    private val httpClient: HttpClient

){
    suspend fun getTenantByPhoneNumber(
        token: String,
        phoneNumber: String,
    ): PrestaSignUserDetailsResponse {
        return signHomeErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetTenantByPhoneNumber.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("phoneNumber", phoneNumber)
                }
            }
        }
    }
}