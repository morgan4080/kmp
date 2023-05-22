package network.onBoarding.client

import components.onBoarding.store.IdentifierTypes
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import network.NetworkConstants
import network.onBoarding.errorHandler.onBoardingErrorHandler
import network.onBoarding.model.PrestaOnBoardingResponse
import network.onBoarding.model.PrestaUpdateMemberResponse

class PrestaOnBoardingClient(
    private val httpClient: HttpClient
) {
    suspend fun getOnBoardingUserData(
        token: String,
        memberIdentifier: String,
        identifierType: IdentifierTypes,
    ): PrestaOnBoardingResponse {
        val identifierTypeSelected = when (identifierType) {
            IdentifierTypes.PHONE_NUMBER -> "PHONE_NUMBER"
            IdentifierTypes.ID_NUMBER -> "ID_NUMBER"
            IdentifierTypes.EMAIL -> "EMAIL"
        }
        return onBoardingErrorHandler {
            httpClient.get(NetworkConstants.PrestaOnBoardingClient.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("memberIdentifier", memberIdentifier)
                    parameters.append("identifierType", identifierTypeSelected)
                    parameters.append("force", "true")
                }
            }
        }
    }

    suspend fun updateOnBoardingMemberPinAndTerms(
        token: String,
        memberRefId: String,
        pinConfirmation: String
    ): PrestaUpdateMemberResponse {
        return onBoardingErrorHandler {
            httpClient.post(NetworkConstants.PrestaUpdatePinTermsClient.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(listOf(
                    "memberRefId" to memberRefId,
                    "termsAccepted" to true,
                    "pin" to pinConfirmation
                ))
            }
        }
    }
}