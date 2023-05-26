package com.presta.customer.network.otp.client

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.otp.errorHandler.otpErrorHandler
import com.presta.customer.network.otp.model.OtpRequestResponse
import com.presta.customer.network.otp.model.OtpVerificationResponse

class PrestaOtpClient(
    private val httpClient: HttpClient
) {
    suspend fun requestOtp(
        token: String,
        phoneNumber: String,
        tenantId: String,
    ): OtpRequestResponse {
        return otpErrorHandler {
            httpClient.post(
                NetworkConstants.PrestaOtpRequestClient.route
            ) {
                if (token !== "") header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("phoneNumber", phoneNumber)
                    parameters.append("appSignature", "C0S9T5M1")
                    parameters.append("tenantId", tenantId)
                }
            }
        }
    }

    suspend fun verifyOtp(
        token: String,
        requestMapper: String,
        otp: String,
        tenantId: String
    ): OtpVerificationResponse {
        return otpErrorHandler {
            httpClient.post(
                "${NetworkConstants.PrestaOtpVerifyClient.route}/${requestMapper}/${otp}"
            ) {
                if (token !== "") header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("tenantId", tenantId)
                }
            }
        }
    }
}