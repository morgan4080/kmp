package com.presta.customer.network.signHome.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.signHome.errorHandler.signHomeErrorHandler
import com.presta.customer.network.signHome.model.Details
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class MemberDetails(
    val details: Details
)

class PrestaSignHomeClient(
    private val httpClient: HttpClient

) {
    suspend fun getTenantByPhoneNumber(
        token: String,
        phoneNumber: String,
    ): PrestaSignUserDetailsResponse {
        return signHomeErrorHandler {
            httpClient.get(NetworkConstants.PrestaGetTenantByPhoneNumber.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, Json)
                contentType(Json)
                url {
                    parameters.append("phoneNumber", phoneNumber)
                }
            }
        }
    }

    suspend fun getTenantByMemberNumber(
        token: String,
        memberNumber: String,
    ): PrestaSignUserDetailsResponse {
        return signHomeErrorHandler {
            httpClient.get("${NetworkConstants.PrestaGetTenantByMemberNumber.route}/${memberNumber}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, Json)
                contentType(Json)
                url {
                    parameters.append("memberNumber", memberNumber)
                }
            }
        }
    }
    suspend fun upDateMemberDetails(
        token: String,
        memberRefId: String,
        details: Details,
    ): PrestaSignUserDetailsResponse {
        return signHomeErrorHandler {
            httpClient.put("${NetworkConstants.PrestaUpdateMemberDetails.route}/${memberRefId}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(Json)
                setBody(
                    MemberDetails(
                        details = details
                    )
                )
            }
        }
    }
}