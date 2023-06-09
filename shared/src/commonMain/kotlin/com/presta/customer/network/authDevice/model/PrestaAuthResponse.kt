package com.presta.customer.network.authDevice.model
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaLogInResponse (
    val session_id: String,
    val access_token: String,
    val refresh_token: String
)
@Serializable
data class RefreshTokenResponse (
    val session_id: String,
    val access_token: String,
    val refresh_token: String,
)
@Serializable
data class AuthUserRoles(
    val realm: List<String>,
    val account: List<String>
)
@Serializable
data class PrestaCheckAuthUserResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val keycloakId: String,
    val username: String,
    @EncodeDefault val email: String? = null,
    val firstName: String,
    val lastName: String,
    val tenantId: String,
    val companyName: String,
    @EncodeDefault val roles: AuthUserRoles? = null
)

