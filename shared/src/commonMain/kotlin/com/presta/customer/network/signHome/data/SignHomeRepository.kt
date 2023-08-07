package com.presta.customer.network.signHome.data

import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse

interface SignHomeRepository {
    suspend fun getTenantByPhoneNumber(
        phoneNumber: String,
        token: String
    ): Result<PrestaSignUserDetailsResponse>

    suspend fun getTenantByMemberNumber(
        memberNumber: String,
        token: String
    ): Result<PrestaSignUserDetailsResponse>

    suspend fun upDateMemberDetails(
        token: String,
        memberRefId: String,
        details:MutableMap<String,String>,
    ): Result<PrestaSignUserDetailsResponse>
}