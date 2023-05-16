package network.client

import network.errorHandler.errorHandler
import network.model.PrestaAuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.formUrlEncode
import network.NetworkConstants
import organisation.Organisation

class PrestaAuthClient(
    private val httpClient: HttpClient
) {

    suspend fun authClient(
        organisation: Organisation,
    ): PrestaAuthResponse {
        return errorHandler {
            httpClient.post(NetworkConstants.PrestaAuthenticateClient.route) {
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf(
                    "client_id" to "direct-access",
                    "grant_type" to "client_credentials",
                    "client_secret" to organisation.client_secret
                ).formUrlEncode())
            }
        }
    }
}