package com.presta.customer.network.otp.data

import com.presta.customer.network.otp.client.PrestaOtpClient
import com.presta.customer.network.otp.model.OtpRequestResponse
import com.presta.customer.network.otp.model.OtpVerificationResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OtpRepositoryImpl: OtpRepository, KoinComponent {
    private val otpClient by inject<PrestaOtpClient>()

    override suspend fun requestOtp(
        token: String,
        phoneNumber: String,
        tenantId: String
    ): Result<OtpRequestResponse> {
        return try {
            val response = otpClient.requestOtp(
                token = token,
                phoneNumber = phoneNumber,
                tenantId = tenantId
            )

            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(
        token: String,
        requestMapper: String,
        otp: String,
        tenantId: String
    ): Result<OtpVerificationResponse> {
        return try {
            val response = otpClient.verifyOtp(
                token = token,
                requestMapper = requestMapper,
                otp = otp,
                tenantId = tenantId
            )

            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}