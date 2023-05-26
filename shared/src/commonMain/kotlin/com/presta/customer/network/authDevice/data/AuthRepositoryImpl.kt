package com.presta.customer.network.authDevice.data

import com.presta.customer.database.dao.UserAuthDao
import com.presta.customer.network.authDevice.client.PrestaAuthClient
import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthRepositoryImpl: AuthRepository, KoinComponent {
    private val prestaAuthClient by inject<PrestaAuthClient>()
    private val userAuthDao by inject<UserAuthDao>()

    override suspend fun loginUser (
        phoneNumber: String,
        pin: String,
        tenantId: String,
        refId: String,
        registrationFees: Double,
        registrationFeeStatus: String
    ): Result<PrestaLogInResponse> {
        return try {
            val response = prestaAuthClient.loginUser(
                phoneNumber,
                pin,
                tenantId
            )

            userAuthDao.removeAccessToken()

            userAuthDao.insert(
                access_token = response.access_token,
                refresh_token = response.refresh_token,
                refId = refId,
                registrationFees = registrationFees,
                registrationFeeStatus = registrationFeeStatus
            )

            Result.success(response)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)

        }
    }

    override suspend fun getCachedUserData(): AuthRepository.ResponseTransform {

        var accessToken = ""
        var refreshToken = ""
        var refId = ""
        var registrationFees = 500.0
        var registrationFeeStatus = "NOT_PAID"

        userAuthDao.selectUserAuthCredentials().map {
            accessToken = it.access_token
            refreshToken = it.access_token
            refId = it.refId
            registrationFees = it.registrationFees
            registrationFeeStatus = it.registrationFeeStatus
        }

        return AuthRepository.ResponseTransform(
            access_token = accessToken,
            refresh_token = refreshToken,
            refId = refId,
            registrationFees = registrationFees,
            registrationFeeStatus = registrationFeeStatus
        )
    }

    override suspend fun checkAuthenticatedUser(token: String): Result<PrestaCheckAuthUserResponse> {
        return try {
            val response = prestaAuthClient.checkAuthUser(token)
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}