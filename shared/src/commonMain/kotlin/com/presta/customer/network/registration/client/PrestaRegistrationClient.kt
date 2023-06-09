package com.presta.customer.network.registration.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.registration.errorHandling.registrationErrorHandler
import com.presta.customer.network.registration.model.PrestaRegistrationResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class PrestaRegistrationClient(
    private val httpClient: HttpClient
) {
    suspend fun createMember(
        token: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        idNumber: String,
        tocsAccepted: Boolean,
        tenantId: String,
    ): PrestaRegistrationResponse {
        return registrationErrorHandler {
            httpClient.post(NetworkConstants.PrestaSelfRegistration.route) {
                if (token !== "") header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    SelfRegistrationData(
                        firstName,
                        lastName,
                        phoneNumber,
                        idNumber,
                        tocsAccepted
                    )
                )
                url {
                    parameters.append("tenantId", tenantId)
                }
            }
        }
    }

    @Serializable
    data class SelfRegistrationData(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val idNumber: String,
        val tocsAccepted: Boolean
    )
}