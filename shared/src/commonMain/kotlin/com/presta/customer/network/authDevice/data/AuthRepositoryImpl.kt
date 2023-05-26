package com.presta.customer.network.authDevice.data

import com.presta.customer.database.dao.UserAuthDao
import com.presta.customer.network.authDevice.client.PrestaAuthClient
import com.presta.customer.network.authDevice.data.dbMapper.toUserAuthEntity
import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse
import com.presta.customer.network.authDevice.model.PrestaCheckPinResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthRepositoryImpl: AuthRepository, KoinComponent {
    private val prestaAuthClient by inject<PrestaAuthClient>()
    private val userAuthDao by inject<UserAuthDao>()

    override suspend fun checkUserPin (
        token: String,
        phoneNumber: String
    ): Result<PrestaCheckPinResponse> {
        return try {
            // if caching functionality check db dao

            // if isEmpty make api request

            val response = prestaAuthClient.checkUserPin(
                token = token,
                phoneNumber = phoneNumber
            )

            // insert to dao

            // respond with dao selectAll

            Result.success(response)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)

        }
    }

    override suspend fun loginUser (
        phoneNumber: String,
        pin: String,
        tenantId: String
    ): Result<PrestaLogInResponse> {
        return try {
            val response = prestaAuthClient.loginUser(
                phoneNumber,
                pin,
                tenantId
            )

            userAuthDao.removeAccessToken()

            userAuthDao.insert(response.toUserAuthEntity())

            Result.success(response)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)

        }
    }

    override suspend fun getUserAuthToken(): Result<PrestaLogInResponse> {
        println(":::::::getting token data")

        userAuthDao.selectUserAuthCredentials().map {
            println(it)
        }

        return Result.success(PrestaLogInResponse(
            access_token = "",
            expires_in = "",
            refresh_expires_in = "",
            refresh_token = "",
            token_type = "",
            scope = ""
        ))
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