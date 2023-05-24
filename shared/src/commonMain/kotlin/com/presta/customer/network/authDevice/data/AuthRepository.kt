package com.presta.customer.network.authDevice.data

import com.presta.customer.network.authDevice.model.PrestaAuthResponse
import com.presta.customer.network.authDevice.model.PrestaCheckPinResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse

interface AuthRepository {
    suspend fun postClientAuthDetails(client_secret: String): Result<PrestaAuthResponse>
    suspend fun checkUserPin(token: String, phoneNumber: String): Result<PrestaCheckPinResponse>
    suspend fun loginUser(phoneNumber: String, pin: String, clientSecret: String): Result<PrestaLogInResponse>
    suspend fun checkAuthenticatedUser(token: String): Result<PrestaCheckAuthUserResponse>
}