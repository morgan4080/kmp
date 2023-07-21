package com.presta.customer.network.longTermLoans.errorHandler

import com.presta.customer.network.authDevice.errorHandler.Message
import com.presta.customer.network.longTermLoans.errors.LongTermLoansError
import com.presta.customer.network.longTermLoans.errors.LongTermLoansExceptions
import com.presta.customer.prestaDispatchers
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext

suspend inline fun <reified T> longTermLoansErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw LongTermLoansExceptions(LongTermLoansError.ServiceUnavailable, null)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw LongTermLoansExceptions(LongTermLoansError.ClientError, null)
        500 -> {
            val data: Message = result.body()
            throw LongTermLoansExceptions(LongTermLoansError.ServerError, "${data.message}: \n ${result.request.url}")
        }
        else -> throw LongTermLoansExceptions(LongTermLoansError.UnknownError, null)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw LongTermLoansExceptions(LongTermLoansError.ServerError, null)
    }

}