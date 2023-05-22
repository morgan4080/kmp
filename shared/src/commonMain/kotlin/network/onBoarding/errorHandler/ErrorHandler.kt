package network.onBoarding.errorHandler

import network.onBoarding.errors.OnBoardingError
import network.onBoarding.errors.OnBoardingException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import prestaDispatchers

suspend inline fun <reified T> onBoardingErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw OnBoardingException(OnBoardingError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw OnBoardingException(OnBoardingError.ClientError)
        500 -> throw OnBoardingException(OnBoardingError.ServerError)
        else -> throw OnBoardingException(OnBoardingError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw OnBoardingException(OnBoardingError.ServerError)
    }

}