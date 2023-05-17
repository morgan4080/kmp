package network.onBoarding.data

import network.onBoarding.model.PrestaOnBoardingResponse

interface OnBoardingRepository {
    suspend fun postOnBoardingData(token: String, memberIdentifier: String, identifierType: String): Result<PrestaOnBoardingResponse>

    // when transactions use flow
    // suspend fun getUserTransactionListFlow(): Flow<List<UserTransactionResponse>>
}