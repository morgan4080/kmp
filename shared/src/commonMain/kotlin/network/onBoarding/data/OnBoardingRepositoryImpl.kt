package network.onBoarding.data

import network.onBoarding.client.PrestaOnBoardingClient
import network.onBoarding.model.PrestaOnBoardingResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnBoardingRepositoryImpl: OnBoardingRepository, KoinComponent {
    private val onBoardingClient by inject<PrestaOnBoardingClient>()

    override suspend fun postOnBoardingData(
        token: String,
        memberIdentifier: String,
        identifierType: String
    ): Result<PrestaOnBoardingResponse> {
        return try {
            // if caching functionality check db dao

            // if isEmpty make api request

            val response = onBoardingClient.onBoardingUser(
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
}