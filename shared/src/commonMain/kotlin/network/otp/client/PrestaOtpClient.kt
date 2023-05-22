package network.otp.client

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import network.NetworkConstants
import network.onBoarding.errorHandler.errorHandler
import network.otp.model.OtpRequestResponse
import network.otp.model.OtpVerificationResponse

class PrestaOtpClient(
    private val httpClient: HttpClient
) {
    suspend fun requestOtp(
        token: String,
        phoneNumber: String
    ): OtpRequestResponse {
        return errorHandler {
            httpClient.post(
                "${NetworkConstants.PrestaOtpRequestClient.route}/${phoneNumber}"
            ) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("appSignature", "KmmCustomerApp")
                }
            }
        }
    }

    suspend fun verifyOtp(
        token: String,
        requestMapper: String,
        otp: String
    ): OtpVerificationResponse {
        return errorHandler {
            httpClient.post(
                "${NetworkConstants.PrestaOtpVerifyClient.route}${requestMapper}/${otp}"
            ) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }
}