package com.presta.customer.network.longTermLoans.data

import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse

interface LongTermLoansRepository {
    suspend fun getLonTermLoansData(
        token: String
    ): Result<PrestaLongTermLoansProductResponse>
}