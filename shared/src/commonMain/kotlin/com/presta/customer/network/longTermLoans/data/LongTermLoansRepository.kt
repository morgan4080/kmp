package com.presta.customer.network.longTermLoans.data

import com.presta.customer.network.longTermLoans.client.DetailsData
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.longTermLoans.model.Guarantor
import com.presta.customer.network.longTermLoans.model.LongTermLoanRequestResponse
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategoriesChildren
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse

interface LongTermLoansRepository {
    suspend fun getLonTermLoansData(
        token: String
    ): Result<PrestaLongTermLoansProductResponse>

    suspend fun getLongTermProductLoanById(
        token: String,
        loanRefId: String,
    ): Result<LongTermLoanResponse>

    suspend fun getLongTermLoansCategoriesData(
        token: String
    ): Result<List<PrestaLongTermLoanCategoriesResponse>>

    suspend fun getLongTermLoanSubCategoriesData(
        token: String,
        parent: String
    ): Result<List<PrestaLongTermLoanSubCategories>>

    suspend fun getLongTermLoanSubCategoriesChildrenData(
        token: String,
        parent: String,
        child: String
    ): Result<List<PrestaLongTermLoanSubCategoriesChildren>>

    suspend fun getClientSettingsData(
        token: String
    ): Result<ClientSettingsResponse>
    suspend fun requestLongTermLoan(
        token: String,
        details: DetailsData,
        loanProductName: String,
        loanProductRefId: String,
        selfCommitment: Double,
        loanAmount: Double,
        memberRefId: String,
        memberNumber: String,
        witnessRefId: String?,
        guarantorList: ArrayList<Guarantor>,
    ): Result<LongTermLoanRequestResponse>
}