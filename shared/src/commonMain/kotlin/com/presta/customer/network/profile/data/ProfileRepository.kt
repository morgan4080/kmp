package com.presta.customer.network.profile.data

import com.presta.customer.network.profile.model.PrestaBalancesResponse

interface ProfileRepository {

    suspend fun getBalancesData(memberRefId:String,token:String): Result<PrestaBalancesResponse>

}