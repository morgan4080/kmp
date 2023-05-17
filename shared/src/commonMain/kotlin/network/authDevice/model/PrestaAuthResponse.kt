package network.authDevice.model
import kotlinx.serialization.Serializable

@Serializable
data class PrestaAuthResponse(
    val access_token: String
)
