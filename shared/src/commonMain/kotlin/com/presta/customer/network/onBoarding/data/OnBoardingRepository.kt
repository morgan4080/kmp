package com.presta.customer.network.onBoarding.data

import com.presta.customer.network.onBoarding.model.PrestaOnBoardingResponse
import com.presta.customer.network.onBoarding.model.PrestaUpdateMemberResponse
import com.presta.customer.ui.components.onBoarding.store.IdentifierTypes

interface OnBoardingRepository {
    suspend fun getOnBoardingMemberData(token: String, memberIdentifier: String, identifierType: IdentifierTypes, tenantId: String): Result<PrestaOnBoardingResponse>
    suspend fun updateOnBoardingMemberPinAndTerms(token: String, memberRefId: String, pinConfirmation: String, tenantId: String): Result<PrestaUpdateMemberResponse>

    // when transactions use flow
    // suspend fun getUserTransactionListFlow(): Flow<List<UserTransactionResponse>>
}