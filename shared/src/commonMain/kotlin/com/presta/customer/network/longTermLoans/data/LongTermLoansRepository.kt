package com.presta.customer.network.longTermLoans.data

import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse

interface LongTermLoansRepository {
    suspend fun getLonTermLoansData(
        token: String
    ): Result<PrestaLongTermLoansProductResponse>

    suspend fun getLongTermProductLoanById(
        token: String,
        loanRefId: String,
    ): Result<LongTermLoanResponse>

    suspend fun getLonTermLoansCategoriesData(
        token: String
    ): Result<List<PrestaLongTermLoanCategoriesResponse>>

}