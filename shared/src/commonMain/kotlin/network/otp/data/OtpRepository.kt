package network.otp.data

import network.otp.model.OtpRequestResponse
import network.otp.model.OtpVerificationResponse

interface OtpRepository {
    suspend fun requestOtp(token: String, phoneNumber: String): Result<OtpRequestResponse>
    suspend fun verifyOtp(token: String, requestMapper: String, otp: String): Result<OtpVerificationResponse>
}