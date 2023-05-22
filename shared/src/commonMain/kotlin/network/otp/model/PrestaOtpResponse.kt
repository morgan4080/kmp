package network.otp.model

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequestResponse(
    val message: String,
    val requestMapper: String,
    val success: Boolean,
    val ttl: Int
)

@Serializable
data class OtpVerificationResponse(
    val message: String
)