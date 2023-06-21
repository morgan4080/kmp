package com.presta.customer.network.loanRequest.errorHandler

import com.presta.customer.network.authDevice.errorHandler.Message
import com.presta.customer.network.loanRequest.errors.LoanRequestError
import com.presta.customer.network.loanRequest.errors.LoanRequestExceptions
import com.presta.customer.prestaDispatchers
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext

suspend inline fun <reified T> loanRequestErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch (e: IOException) {
        throw LoanRequestExceptions(LoanRequestError.ServiceUnavailable, null)
    }

    when (result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw LoanRequestExceptions(LoanRequestError.ClientError, null)
        500 -> {
            val data: Message = result.body()
            throw LoanRequestExceptions(LoanRequestError.ServerError, "${data.message}: \n ${result.request.url}")
        }
        else -> throw LoanRequestExceptions(LoanRequestError.UnknownError, null)
    }

    return@withContext try {
        result.body()
    } catch (e: Exception) {
        throw LoanRequestExceptions(LoanRequestError.ServerError, null)
    }

}