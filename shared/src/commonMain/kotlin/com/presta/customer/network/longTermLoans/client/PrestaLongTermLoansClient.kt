package com.presta.customer.network.longTermLoans.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.longTermLoans.errorHandler.longTermLoansErrorHandler
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class PrestaLongTermLoansClient(
    private val httpClient: HttpClient
){
    suspend fun getLongTermLoansData (
        token: String,
    ): List<PrestaLongTermLoansProductResponse> {
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
}