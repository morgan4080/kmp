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
import network.onBoarding.errorHandler.errorHandler
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
        val idTypes = when(identifierType) {
            IdentifierTypes.PHONE_NUMBER -> "PHONE_NUMBER"
            IdentifierTypes.ID_NUMBER -> "ID_NUMBER"
            IdentifierTypes.EMAIL -> "EMAIL"
        }
        return errorHandler {
            httpClient.get(NetworkConstants.PrestaOnBoardingClient.route) {
                url {
                    parameters.append("memberIdentifier", memberIdentifier)
                    parameters.append("identifierType", idTypes)
                    parameters.append("force", "true")
                }
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun updateOnBoardingMemberPinAndTerms(
        token: String,
        memberRefId: String,
        pinConfirmation: String
    ): PrestaUpdateMemberResponse {
        return errorHandler {
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