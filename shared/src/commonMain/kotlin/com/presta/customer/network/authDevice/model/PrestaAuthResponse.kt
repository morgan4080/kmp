package com.presta.customer.network.authDevice.model
import kotlinx.serialization.Serializable

@Serializable
data class PrestaAuthResponse(
    val access_token: String,
    val expires_in: Int,
    val refresh_expires_in: Int
)

// {"data":{"hasPin":true,"pinStatus":"SET","ussdPhoneNumber":"254720753971"},"messages":[{"type":"SUCCESS","code":"ok","message":"Completed Successfully","isTechnical":false}]}
@Serializable
data class PrestaCheckPinResponseData(val hasPin: Boolean, val pinStatus: String, val ussdPhoneNumber: String)
@Serializable
data class PrestaCheckPinResponse(
    val data: PrestaCheckPinResponseData,
)
@Serializable
data class PrestaLogInResponse (
    val access_token: String,
)
@Serializable
data class AuthUserRoles(
    val realm: List<String>,
    val account: List<String>
)
@Serializable
data class PrestaCheckAuthUserResponse (
    val keycloakId: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val tenantId: String,
    val companyName: String,
    val roles: AuthUserRoles
)


