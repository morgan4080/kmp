package com.presta.customer.network.registration.errorHandling

import com.presta.customer.network.registration.errors.RegistrationError
import com.presta.customer.network.registration.errors.RegistrationException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import com.presta.customer.prestaDispatchers
import io.ktor.client.statement.request

/*{
    "timestamp":"29-05-2023 02:43:43",
    "code":0,
    "message":"{\"timestamp\":\"29-05-2023 05:43:43\",\"code\":500,\"status\":\"INTERNAL_SERVER_ERROR\",\"message\":\"could not execute statement; SQL [n/a]; constraint [idnumber_UNIQUE]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement\"
    ,\"isTechnical\":true}"
}*/
@Serializable
data class RegistrationResponse(
    val timestamp: String,
    val code: Int,
    val message: String,
)

suspend inline fun <reified T> registrationErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw RegistrationException(RegistrationError.ServiceUnavailable, null)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw RegistrationException(RegistrationError.ClientError, null)
        500 -> {
            val data: RegistrationResponse = result.body()
            throw RegistrationException(RegistrationError.ServerError, "${data.message}: \n ${result.request.url}")
        }
        else -> throw RegistrationException(RegistrationError.UnknownError, null)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw RegistrationException(RegistrationError.ServerError, null)
    }

}