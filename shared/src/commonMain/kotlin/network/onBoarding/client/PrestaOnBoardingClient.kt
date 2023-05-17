package network.onBoarding.client

import network.onBoarding.errorHandler.errorHandler
import network.onBoarding.model.PrestaOnBoardingResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import network.NetworkConstants

class PrestaOnBoardingClient(
    private val httpClient: HttpClient
) {
    suspend fun onBoardingUser(
        token: String,
        memberIdentifier: String,
        identifierType: String,
    ): PrestaOnBoardingResponse {
        return errorHandler {
            httpClient.get(NetworkConstants.PrestaOnBoardingClient.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                url {
                    parameters.append("memberIdentifier", memberIdentifier)
                    parameters.append("identifierType", identifierType)
                    parameters.append("force", "true")
                }
                contentType(ContentType.Application.Json)
            }
        }
    }
}