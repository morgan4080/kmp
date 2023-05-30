package com.presta.customer.network.shortTermLoans.errorHandler

import com.presta.customer.network.shortTermLoans.errors.ShortTermLoansError
import com.presta.customer.network.shortTermLoans.errors.ShortTermLoansExceptions
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import prestaDispatchers

suspend inline fun <reified T> shortTermLoansErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw ShortTermLoansExceptions(ShortTermLoansError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw ShortTermLoansExceptions(ShortTermLoansError.ClientError)
        500 -> throw ShortTermLoansExceptions(ShortTermLoansError.ServerError)
        else -> throw ShortTermLoansExceptions(ShortTermLoansError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw ShortTermLoansExceptions(ShortTermLoansError.ServerError)
    }

}