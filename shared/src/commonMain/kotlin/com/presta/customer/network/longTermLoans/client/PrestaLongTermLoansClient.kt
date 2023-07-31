package com.presta.customer.network.longTermLoans.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.longTermLoans.errorHandler.longTermLoansErrorHandler
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategoriesChildren
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

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
}