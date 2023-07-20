package com.presta.customer.network.signHome.errorHandler

import com.presta.customer.network.authDevice.errorHandler.Message
import com.presta.customer.network.signHome.errors.SignHomeError
import com.presta.customer.network.signHome.errors.SignHomeException
import com.presta.customer.prestaDispatchers
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext

suspend inline fun <reified T> signHomeErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {
    val result = try {
        response()
    } catch(e: IOException) {
        throw SignHomeException(SignHomeError.ServiceUnavailable, null)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw SignHomeException(SignHomeError.ClientError, null)
        500 -> {
            val data: Message = result.body()
            throw SignHomeException(SignHomeError.ServerError, "${data.message}: \n ${result.request.url}")
        }
        else -> throw SignHomeException(SignHomeError.UnknownError, null)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw SignHomeException(SignHomeError.ServerError, null)
    }

}