package network.onBoarding.data

import components.onBoarding.store.IdentifierTypes
import network.onBoarding.model.PrestaOnBoardingResponse
import network.onBoarding.model.PrestaUpdateMemberResponse

interface OnBoardingRepository {
    suspend fun getOnBoardingMemberData(token: String, memberIdentifier: String, identifierType: IdentifierTypes): Result<PrestaOnBoardingResponse>
    suspend fun updateMemberData(token: String, memberRefId: String, pinConfirmation: String): Result<PrestaUpdateMemberResponse>

    // when transactions use flow
    // suspend fun getUserTransactionListFlow(): Flow<List<UserTransactionResponse>>
}