package com.presta.customer.network.onBoarding.errorHandler

import com.presta.customer.network.onBoarding.errors.OnBoardingError
import com.presta.customer.network.onBoarding.errors.OnBoardingException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import com.presta.customer.prestaDispatchers
@Serializable
data class ConfigResponse(val timestamp: String, val code: Int, val message: String)
suspend inline fun <reified T> onBoardingErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw OnBoardingException(OnBoardingError.ServiceUnavailable, null)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> {
            val data: ConfigResponse = result.body()
            throw OnBoardingException(OnBoardingError.ClientError, data.message)
        }
        500 -> {
            val data: ConfigResponse = result.body()
            throw OnBoardingException(OnBoardingError.ServerError, data.message)
        }
        else -> throw OnBoardingException(OnBoardingError.UnknownError, null)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw OnBoardingException(OnBoardingError.ServerError, null)
    }

}