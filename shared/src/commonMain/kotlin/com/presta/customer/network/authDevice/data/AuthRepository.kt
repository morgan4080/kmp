package com.presta.customer.network.authDevice.data

import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import com.presta.customer.network.authDevice.model.RefreshTokenResponse
import com.presta.customer.network.onBoarding.model.RegistrationFeeStatus

interface AuthRepository {
    suspend fun loginUser(phoneNumber: String, pin: String, tenantId: String, refId: String, registrationFees: Double, registrationFeeStatus: String): Result<PrestaLogInResponse>
    suspend fun updateAuthToken(tenantId: String, refId: String): Result<RefreshTokenResponse>
    suspend fun checkAuthenticatedUser(token: String): Result<PrestaCheckAuthUserResponse>
    suspend fun getCachedUserData(): ResponseTransform
    suspend fun logOutUser(): LogOutResponse

    data class LogOutResponse(
        val loggedOut: Boolean = false
    )

    data class ResponseTransform(
        val refId: String,
        val access_token: String,
        val refresh_token: String,
        val registrationFees: Double,
        val registrationFeeStatus: String,
        val phoneNumber: String,
    )
}