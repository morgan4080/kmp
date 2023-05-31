package com.presta.customer.network.profile.data

import com.presta.customer.network.profile.client.PrestaProfileClient
import com.presta.customer.network.profile.model.PrestaLoansBalancesResponse
import com.presta.customer.network.profile.model.PrestaSavingsBalancesResponse
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileRepositoryImpl : ProfileRepository,KoinComponent {
    private val profileClient by inject<PrestaProfileClient>()

    override suspend fun getUserSavingsData(
        memberRefId: String,
        token: String
    ): Result<PrestaSavingsBalancesResponse> {
        return try {
            val response = profileClient.getUserSavingsData (
                token = token,
                memberRefId = memberRefId
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getUserLoansData(
        memberRefId: String,
        token: String
    ): Result<PrestaLoansBalancesResponse> {
        return try {
            val response = profileClient.getUserLoansData (
                token = token,
                memberRefId = memberRefId
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getTransactionHistoryData(
        memberRefId: String,
        token: String,
        purposeIds: List<String>
    ): Result<List<PrestaTransactionHistoryResponse>> {
        return try {
            val response = profileClient.getUserTransactionHistoryData(
                token = token,
                memberRefId = memberRefId,
                purposeIds = purposeIds
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getTransactionMappingData(token: String): Result<Map<String, String>> {
        return try {
            val response = profileClient.getTransactionsMappingData(token = token)
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}