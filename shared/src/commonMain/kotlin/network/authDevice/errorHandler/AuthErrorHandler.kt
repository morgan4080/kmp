package network.authDevice.errorHandler

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import network.authDevice.errors.AuthClientError
import network.authDevice.errors.AuthClientException
import prestaDispatchers

suspend inline fun <reified T> authErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw AuthClientException(AuthClientError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw AuthClientException(AuthClientError.ClientError)
        500 -> throw AuthClientException(AuthClientError.ServerError)
        else -> throw AuthClientException(AuthClientError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw AuthClientException(AuthClientError.ServerError)
    }

}