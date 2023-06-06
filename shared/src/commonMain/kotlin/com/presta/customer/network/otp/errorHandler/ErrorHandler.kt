package com.presta.customer.network.otp.errorHandler

import com.presta.customer.network.otp.errors.OtpError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import com.presta.customer.network.otp.errors.OtpException
import com.presta.customer.prestaDispatchers

suspend inline fun <reified T> otpErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw OtpException(OtpError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw OtpException(OtpError.ClientError)
        500 -> throw OtpException(OtpError.ServerError)
        else -> throw OtpException(OtpError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw OtpException(OtpError.ServerError)
    }

}