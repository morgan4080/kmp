package com.presta.customer.network.tenant.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.profile.errorHandler.profileErrorHandler
import com.presta.customer.network.tenant.model.PrestaTenantResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class PrestaTenantClient(
    private val httpClient: HttpClient
) {
    //Get Tenant By Id
    suspend fun getTenantById(
        searchTerm: String,
    ): PrestaTenantResponse {
        return profileErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetTenantById.route) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("searchTerm",  searchTerm)
                }
            }
        }
    }
}


