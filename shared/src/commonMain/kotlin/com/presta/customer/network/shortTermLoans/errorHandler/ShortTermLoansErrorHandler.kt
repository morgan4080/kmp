package com.presta.customer.network.shortTermLoans.errorHandler

import com.presta.customer.network.authDevice.errorHandler.Message
import com.presta.customer.network.shortTermLoans.errors.ShortTermLoansError
import com.presta.customer.network.shortTermLoans.errors.ShortTermLoansExceptions
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import com.presta.customer.prestaDispatchers
import io.ktor.client.statement.request

suspend inline fun <reified T> shortTermLoansErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw ShortTermLoansExceptions(ShortTermLoansError.ServiceUnavailable, null)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw ShortTermLoansExceptions(ShortTermLoansError.ClientError, null)
        500 -> {
            val data: Message = result.body()
            throw ShortTermLoansExceptions(ShortTermLoansError.ServerError, "${data.message}: \n ${result.request.url}")
        }
        else -> throw ShortTermLoansExceptions(ShortTermLoansError.UnknownError, null)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw ShortTermLoansExceptions(ShortTermLoansError.ServerError, null)
    }

}