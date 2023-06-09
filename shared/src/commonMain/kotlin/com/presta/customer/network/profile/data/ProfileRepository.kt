package com.presta.customer.network.profile.data

import com.presta.customer.network.profile.model.PrestaLoansBalancesResponse
import com.presta.customer.network.profile.model.PrestaSavingsBalancesResponse
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse

interface ProfileRepository {
    suspend fun getUserSavingsData(memberRefId: String, token:String): Result<PrestaSavingsBalancesResponse>
    suspend fun getUserLoansData(memberRefId: String, token:String): Result<PrestaLoansBalancesResponse>
    suspend fun getTransactionHistoryData(memberRefId: String, token: String, purposeIds: List<String>, searchTerm: String?): Result<List<PrestaTransactionHistoryResponse>>
    suspend fun getTransactionMappingData(token: String): Result<Map<String, String>>
}