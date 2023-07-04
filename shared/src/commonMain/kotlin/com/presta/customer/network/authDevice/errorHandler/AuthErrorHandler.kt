package com.presta.customer.network.authDevice.errorHandler

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import com.presta.customer.network.authDevice.errors.AuthClientError
import com.presta.customer.network.authDevice.errors.AuthClientException
import kotlinx.serialization.Serializable
import com.presta.customer.prestaDispatchers
import io.ktor.client.statement.request

@Serializable
data class Message(val message: String, val code: Int, val timestamp: String)

suspend inline fun <reified T> authErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw AuthClientException(AuthClientError.ServiceUnavailable, null)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> {
            throw AuthClientException(AuthClientError.ClientError, null)
        }
        500 -> {
            val data: Message = result.body()
            throw AuthClientException(AuthClientError.ServerError, "${data.message}: \n ${result.request.url}")
        }
        else -> {
            throw AuthClientException(AuthClientError.UnknownError, null)
        }
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw AuthClientException(AuthClientError.ServerError, null)
    }

}