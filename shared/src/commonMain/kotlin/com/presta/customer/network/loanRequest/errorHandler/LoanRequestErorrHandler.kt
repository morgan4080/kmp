package com.presta.customer.network.loanRequest.errorHandler

import com.presta.customer.network.loanRequest.errors.LoanRequestError
import com.presta.customer.network.loanRequest.errors.LoanRequestExceptions
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import prestaDispatchers

suspend inline fun <reified T> loanRequestErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw LoanRequestExceptions(LoanRequestError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw LoanRequestExceptions(LoanRequestError.ClientError)
        500 -> throw LoanRequestExceptions(LoanRequestError.ServerError)
        else -> throw LoanRequestExceptions(LoanRequestError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw LoanRequestExceptions(LoanRequestError.ServerError)
    }

}