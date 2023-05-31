package com.presta.customer.network.shortTermLoans.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.shortTermLoans.errorHandler.shortTermLoansErrorHandler
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermTopUpListResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class PrestaShortTermLoansClient (
    private val httpClient: HttpClient
        ){
    suspend fun getShortTermProductsList(
        token: String,
        memberRefId: String,
    ): List<PrestaShortTermProductsListResponse> {
        return shortTermLoansErrorHandler {
            httpClient.get("${NetworkConstants.PrestaGetTShortTermProductsList.route}/${memberRefId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }
    suspend fun getShortTermTopUpList(
        token: String,
        memberRefId: String,
        session_id:String
    ): List<PrestaShortTermTopUpListResponse> {
        return shortTermLoansErrorHandler {
            httpClient.get("${NetworkConstants.PrestaGetShortTermToUpList.route}/${session_id}${memberRefId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }
}