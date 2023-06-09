package com.presta.customer.network.onBoarding.data

import com.presta.customer.network.onBoarding.client.PrestaOnBoardingClient
import com.presta.customer.network.onBoarding.model.PrestaOnBoardingResponse
import com.presta.customer.network.onBoarding.model.PrestaUpdateMemberResponse
import com.presta.customer.ui.components.onBoarding.store.IdentifierTypes
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnBoardingRepositoryImpl: OnBoardingRepository, KoinComponent {
    private val onBoardingClient by inject<PrestaOnBoardingClient>()

    override suspend fun getOnBoardingMemberData(
        token: String,
        memberIdentifier: String,
        identifierType: IdentifierTypes,
        tenantId: String,
    ): Result<PrestaOnBoardingResponse> {
        return try {
            // if caching functionality check db dao

            // if isEmpty make api request

            val response = onBoardingClient.getOnBoardingUserData(
                token = token,
                memberIdentifier = memberIdentifier,
                identifierType = identifierType,
                tenantId = tenantId
            )

            // insert to dao

            // respond with dao selectAll

            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun updateOnBoardingMemberPinAndTerms(
        token: String,
        memberRefId: String,
        pinConfirmation: String,
        tenantId: String
    ): Result<PrestaUpdateMemberResponse> {
        return try {

            val response = onBoardingClient.updateOnBoardingMemberPinAndTerms(
                token = token,
                memberRefId = memberRefId,
                pinConfirmation = pinConfirmation,
                tenantId = tenantId
            )

            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}