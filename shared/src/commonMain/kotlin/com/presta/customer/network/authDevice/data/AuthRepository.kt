package com.presta.customer.network.authDevice.data

import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import com.presta.customer.network.onBoarding.model.RegistrationFeeStatus

interface AuthRepository {
    suspend fun loginUser(phoneNumber: String, pin: String, tenantId: String, refId: String, registrationFees: Double, registrationFeeStatus: String): Result<PrestaLogInResponse>
    suspend fun checkAuthenticatedUser(token: String): Result<PrestaCheckAuthUserResponse>
    suspend fun getCachedUserData(): ResponseTransform

    data class ResponseTransform(
        val refId: String,
        val access_token: String,
        val refresh_token: String,
        val registrationFees: Double,
        val registrationFeeStatus: String,
    )
}