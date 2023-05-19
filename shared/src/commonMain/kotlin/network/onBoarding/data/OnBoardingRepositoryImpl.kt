package network.onBoarding.data

import components.onBoarding.store.IdentifierTypes
import network.onBoarding.client.PrestaOnBoardingClient
import network.onBoarding.model.PrestaOnBoardingResponse
import network.onBoarding.model.PrestaUpdateMemberResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnBoardingRepositoryImpl: OnBoardingRepository, KoinComponent {
    private val onBoardingClient by inject<PrestaOnBoardingClient>()

    override suspend fun getOnBoardingMemberData(
        token: String,
        memberIdentifier: String,
        identifierType: IdentifierTypes
    ): Result<PrestaOnBoardingResponse> {
        return try {
            // if caching functionality check db dao

            // if isEmpty make api request

            val response = onBoardingClient.getOnBoardingUserData(
                token = token,
                memberIdentifier = memberIdentifier,
                identifierType = identifierType
            )

            // insert to dao

            // respond with dao selectAll

            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun updateMemberData(
        token: String,
        memberRefId: String,
        pinConfirmation: String
    ): Result<PrestaUpdateMemberResponse> {
        return try {

            val response = onBoardingClient.updateOnBoardingMemberPinAndTerms(
                token = token,
                memberRefId = memberRefId,
                pinConfirmation = pinConfirmation
            )

            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}