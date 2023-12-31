package com.presta.customer.network.signHome.data

import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
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

    suspend fun upDateMemberPersonalInfo(
        token: String,
        memberRefId: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        idNumber: String,
        email: String
    ): Result<PrestaSignUserDetailsResponse>
    suspend fun getClientSettingsData(
        token: String
    ): Result<ClientSettingsResponse>
}