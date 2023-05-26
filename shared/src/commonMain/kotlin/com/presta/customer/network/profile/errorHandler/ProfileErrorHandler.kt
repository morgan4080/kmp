package com.presta.customer.network.profile.errorHandler

import com.presta.customer.network.profile.errors.ProfileError
import com.presta.customer.network.profile.errors.ProfileException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import prestaDispatchers

suspend inline fun <reified T> profileErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw ProfileException(ProfileError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw ProfileException(ProfileError.ClientError)
        500 -> throw ProfileException(ProfileError.ServerError)
        else -> throw ProfileException(ProfileError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw ProfileException(ProfileError.ServerError)
    }

}