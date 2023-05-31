package com.presta.customer.network.shortTermLoans.data

import com.presta.customer.network.shortTermLoans.client.PrestaShortTermLoansClient
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermTopUpListResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ShortTermLoansRepositoryImpl : ShortTermLoansRepository,KoinComponent {
    private val shortTermLoansClient by inject<PrestaShortTermLoansClient>()
    override suspend fun getShortTermProductListData(
        memberRefId: String,
        token: String
    ): Result<List<PrestaShortTermProductsListResponse>> {

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

    override suspend fun getShortTermTopUpListData(
        session_id: String,
        memberRefId: String,
        token: String
    ): Result<PrestaShortTermTopUpListResponse> {

        return try {
            val response = shortTermLoansClient.getShortTermTopUpList(
                token = token,
                memberRefId = memberRefId,
                session_id =session_id
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}