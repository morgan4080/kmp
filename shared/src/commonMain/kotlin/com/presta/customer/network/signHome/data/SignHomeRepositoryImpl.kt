package com.presta.customer.network.signHome.data

import com.presta.customer.network.signHome.client.PrestaSignHomeClient
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignHomeRepositoryImpl : SignHomeRepository, KoinComponent {
    private val prestaSignHomeClient by inject<PrestaSignHomeClient>()
    override suspend fun getTenantByPhoneNumber(
        phoneNumber: String,
        token: String
    ): Result<PrestaSignUserDetailsResponse> {
        return try {
            val response = prestaSignHomeClient.getTenantByPhoneNumber(
                token = token,
                phoneNumber = phoneNumber
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getTenantByMemberNumber(
        memberNumber: String,
        token: String
    ): Result<PrestaSignUserDetailsResponse> {
        return try {
            val response = prestaSignHomeClient.getTenantByMemberNumber(
                token = token,
                memberNumber = memberNumber
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun upDateMemberDetails(
        token: String,
        memberRefId: String,
       details:MutableMap<String,String>
    ): Result<PrestaSignUserDetailsResponse> {
        return try {
            val response = prestaSignHomeClient.upDateMemberDetails(
                token = token,
                memberRefId = memberRefId,
                details = details

            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    override suspend fun upDateMemberPersonalInfo(
        token: String,
        memberRefId: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        idNumber: String,
        email: String
    ): Result<PrestaSignUserDetailsResponse> {
        return try {
            val response = prestaSignHomeClient.upDateMemberPersonalInformation(
                token = token,
                memberRefId = memberRefId,
                firstName =firstName,
                lastName= lastName,
                phoneNumber= phoneNumber,
                idNumber= idNumber,
                email= email
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}