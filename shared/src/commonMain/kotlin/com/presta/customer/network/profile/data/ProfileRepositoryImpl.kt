package com.presta.customer.network.profile.data

import com.presta.customer.network.profile.client.PrestaProfileClient
import com.presta.customer.network.profile.model.PrestaBalancesResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileRepositoryImpl : ProfileRepository,KoinComponent {
    private val profileClient by inject<PrestaProfileClient>()

    override suspend fun getBalancesData(
        memberRefId: String,
        token: String
    ): Result<PrestaBalancesResponse> {

        return try {
            // if caching functionality check db dao

            // if isEmpty make api request
            //Get data  From The APi

            val response = profileClient.getUserSavingsData(
                token = token,
                memberIdentifier = memberRefId

                )

            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }


    }
}