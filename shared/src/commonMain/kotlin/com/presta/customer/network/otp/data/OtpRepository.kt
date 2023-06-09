package com.presta.customer.network.otp.data

import com.presta.customer.network.otp.model.OtpRequestResponse
import com.presta.customer.network.otp.model.OtpVerificationResponse

interface OtpRepository {
    suspend fun requestOtp(token: String, phoneNumber: String, tenantId: String): Result<OtpRequestResponse>
    suspend fun verifyOtp(token: String, requestMapper: String, otp: String, tenantId: String): Result<OtpVerificationResponse>
}