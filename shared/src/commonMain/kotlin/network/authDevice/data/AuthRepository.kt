package network.authDevice.data

import network.authDevice.model.PrestaAuthResponse

interface AuthRepository {
    suspend fun postClientAuthDetails(client_secret: String): Result<PrestaAuthResponse>
}