package com.presta.customer.network.authDevice.model
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaLogInResponse (
    val session_id: String,
    val access_token: String,
    val expires_in: Long,
    val refresh_expires_in: Long,
    val refresh_token: String,
)
@Serializable
data class RefreshTokenResponse (
    val session_id: String,
    val access_token: String,
    val expires_in: Long,
    val refresh_expires_in: Long,
    val refresh_token: String,
)
enum class PrestaServices {
    EGUARANTORSHIP, EASYUSSD, PRESTALENDER, PRESTAPAY, PRESTAAPPRAISAL, USERSADMIN
}
enum class ServicesActivity {
    ACTIVE, INACTIVE
}
@Serializable
data class TenantServicesResponse (
    val name: PrestaServices,
    val status: ServicesActivity
)
enum class TenantServiceConfig {
    savings, shares, membershipFees
}
@Serializable
data class TenantServiceConfigResponse (
    val name: TenantServiceConfig,
    val status: Boolean
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

