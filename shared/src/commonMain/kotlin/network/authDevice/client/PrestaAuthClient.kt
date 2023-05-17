package network.authDevice.client

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.formUrlEncode
import network.NetworkConstants
import network.authDevice.errorHandler.authErrorHandler
import network.authDevice.model.PrestaAuthResponse

class PrestaAuthClient(
    private val httpClient: HttpClient
) {

    suspend fun authClient(
        client_secret: String,
    ): PrestaAuthResponse {
        return authErrorHandler {
            httpClient.post(NetworkConstants.PrestaAuthenticateClient.route) {
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf(
                    "client_id" to "direct-access",
                    "grant_type" to "client_credentials",
                    "client_secret" to client_secret
                ).formUrlEncode())
            }
        }
    }
}