package network.authDevice.data

import network.authDevice.client.PrestaAuthClient
import network.authDevice.model.PrestaAuthResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthRepositoryImpl: AuthRepository, KoinComponent {
    private val prestaAuthClient by inject<PrestaAuthClient>()

    override suspend fun postClientAuthDetails(client_secret: String): Result<PrestaAuthResponse> {
        return try {
            // if caching functionality check db dao

            // if isEmpty make api request

            val response = prestaAuthClient.authClient(client_secret = client_secret)

            // insert to dao

            // respond with dao selectAll

            Result.success(response)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)

        }
    }
}