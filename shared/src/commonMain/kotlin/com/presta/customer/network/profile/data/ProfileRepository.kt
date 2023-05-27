package com.presta.customer.network.profile.data

import com.presta.customer.network.profile.model.PrestaBalancesResponse
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse

interface ProfileRepository {

    suspend fun getBalancesData(memberRefId:String,token:String): Result<PrestaBalancesResponse>
    suspend fun getTransactionHistoryData(memberRefId:String,token:String):Result<PrestaTransactionHistoryResponse>

}