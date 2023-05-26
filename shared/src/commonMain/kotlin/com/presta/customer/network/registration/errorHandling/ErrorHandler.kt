package com.presta.customer.network.registration.errorHandling

import com.presta.customer.network.registration.errors.RegistrationError
import com.presta.customer.network.registration.errors.RegistrationException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import prestaDispatchers

suspend inline fun <reified T> registrationErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw RegistrationException(RegistrationError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw RegistrationException(RegistrationError.ClientError)
        500 -> throw RegistrationException(RegistrationError.ServerError)
        else -> throw RegistrationException(RegistrationError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw RegistrationException(RegistrationError.ServerError)
    }

}