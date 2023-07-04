package com.presta.customer.network.profile.errorHandler

import com.presta.customer.network.authDevice.errorHandler.Message
import com.presta.customer.network.profile.errors.ProfileError
import com.presta.customer.network.profile.errors.ProfileException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import com.presta.customer.prestaDispatchers
import io.ktor.client.statement.request

suspend inline fun <reified T> profileErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {
    val result = try {
        response()
    } catch(e: IOException) {
        throw ProfileException(ProfileError.ServiceUnavailable, null)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw ProfileException(ProfileError.ClientError, null)
        500 -> {
            val data: Message = result.body()
            throw ProfileException(ProfileError.ServerError, "${data.message}: \n ${result.request.url}")
        }
        else -> throw ProfileException(ProfileError.UnknownError, null)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw ProfileException(ProfileError.ServerError, null)
    }

}