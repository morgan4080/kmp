package com.presta.customer.network.authDevice.data

import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse

interface AuthRepository {
    suspend fun loginUser(phoneNumber: String, pin: String, tenantId: String): Result<PrestaLogInResponse>
    suspend fun checkAuthenticatedUser(token: String): Result<PrestaCheckAuthUserResponse>
    suspend fun getUserAuthToken(): Result<PrestaLogInResponse>
}