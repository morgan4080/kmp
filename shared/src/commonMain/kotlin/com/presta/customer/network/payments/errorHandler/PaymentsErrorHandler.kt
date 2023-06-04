package com.presta.customer.network.payments.errorHandler

import com.presta.customer.network.payments.errors.PaymentsError
import com.presta.customer.network.payments.errors.PaymentsException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import prestaDispatchers

suspend inline fun <reified T> paymentsErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw PaymentsException(PaymentsError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw PaymentsException(PaymentsError.ClientError)
        500 -> throw PaymentsException(PaymentsError.ServerError)
        else -> throw PaymentsException(PaymentsError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw PaymentsException(PaymentsError.ServerError)
    }

}