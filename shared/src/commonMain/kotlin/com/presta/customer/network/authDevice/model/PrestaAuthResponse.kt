package com.presta.customer.network.authDevice.model
import kotlinx.serialization.Serializable

@Serializable
data class PrestaLogInResponse (
    val access_token: String,
    val refresh_token: String
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


