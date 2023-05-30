package com.presta.customer.network.shortTermLoans.data

import com.presta.customer.network.shortTermLoans.client.PrestaShortTermLoansClient
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ShortTermLoansRepositoryImpl : ShortTermLoansRepository,KoinComponent {
    private val shortTermLoansClient by inject<PrestaShortTermLoansClient>()

    override suspend fun getShortTermProductListData(
        memberRefId: String,
        token: String
    ): Result<PrestaShortTermProductsListResponse> {

        return try {
            // if caching functionality check db dao
            // if isEmpty make api request
            //Get data  From The APi
            val response = shortTermLoansClient.getShortTermProductsList (
                token = token,
                memberRefId = memberRefId
            )

            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    }
}