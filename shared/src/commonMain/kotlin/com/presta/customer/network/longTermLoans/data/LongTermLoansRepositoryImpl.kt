package com.presta.customer.network.longTermLoans.data

import com.presta.customer.network.longTermLoans.client.PrestaLongTermLoansClient
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LongTermLoansRepositoryImpl : LongTermLoansRepository, KoinComponent {
    private val prestaLongTermLoansClient by inject<PrestaLongTermLoansClient>()
    override suspend fun getLonTermLoansData(
        token: String
    ): Result<PrestaLongTermLoansProductResponse> {
        return try {
            val response = prestaLongTermLoansClient.getLongTermLoansData(
                token = token
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    }
}