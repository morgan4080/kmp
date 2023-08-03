package com.presta.customer.network.longTermLoans.data

import com.presta.customer.network.longTermLoans.client.DetailsData
import com.presta.customer.network.longTermLoans.client.PrestaLongTermLoansClient
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.longTermLoans.model.Guarantor
import com.presta.customer.network.longTermLoans.model.LongTermLoanRequestResponse
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategoriesChildren
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import com.presta.customer.network.signHome.model.Details
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
    override suspend fun getLongTermProductLoanById(
        token: String,
        loanRefId: String
    ): Result<LongTermLoanResponse> {
        return try {
            val response = prestaLongTermLoansClient.getLongTermProductLoanById(
                token = token,
                loanRefId = loanRefId
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getLongTermLoansCategoriesData(
        token: String
    ): Result<List<PrestaLongTermLoanCategoriesResponse>> {
        return try {
            val response = prestaLongTermLoansClient.getLoanCategories(
                token = token
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getLongTermLoanSubCategoriesData(
        token: String,
        parent: String
    ): Result<List<PrestaLongTermLoanSubCategories>> {
        return try {
            val response = prestaLongTermLoansClient.getLoanSubCategories(
                token = token,
                parent = parent
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    override suspend fun getLongTermLoanSubCategoriesChildrenData(
        token: String,
        parent: String,
        child: String
    ): Result<List<PrestaLongTermLoanSubCategoriesChildren>> {
        return try {
            val response = prestaLongTermLoansClient.getLoanSubCategoriesChildren(
                token = token,
                parent = parent,
                child = child
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getClientSettingsData(
        token: String
    ): Result<ClientSettingsResponse> {
        return try {
            val response = prestaLongTermLoansClient.getClientSettings(
                token = token
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }


    override suspend fun requestLongTermLoan(
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
    ): Result<LongTermLoanRequestResponse> {
        return try {
            val response = prestaLongTermLoansClient.sendLongTermLoanRequest(
                token = token,
                details = details,
                loanProductName = loanProductName,
                loanProductRefId = loanProductRefId,
                selfCommitment = selfCommitment,
                loanAmount = loanAmount,
                memberRefId = memberRefId,
                memberNumber = memberNumber,
                witnessRefId = witnessRefId,
                guarantorList = guarantorList,
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}