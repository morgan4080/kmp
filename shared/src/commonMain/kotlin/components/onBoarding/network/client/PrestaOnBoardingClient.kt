package components.onBoarding.network.client

import network.errorHandler.errorHandler
import components.onBoarding.network.model.PrestaOnBoardingResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import network.NetworkConstants

class PrestaOnBoardingClient(
    private val httpClient: HttpClient
) {
    suspend fun onBoardingUser(
        memberIdentifier: String,
        identifierType: String,
    ): PrestaOnBoardingResponse {
        return errorHandler {
            httpClient.get(NetworkConstants.PrestaOnBoardingClient.route) {
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