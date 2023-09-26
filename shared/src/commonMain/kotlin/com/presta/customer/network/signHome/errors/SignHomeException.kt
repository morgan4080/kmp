package com.presta.customer.network.signHome.errors

import com.presta.customer.Platform
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

enum class SignHomeError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}
class SignHomeException(error: SignHomeError, message: String?): Exception(
    "Data loading  Error: ${if (message !== null) message else error}"
),KoinComponent{
    val platform by inject<Platform>()
    init {
        if (message !== null) platform.logErrorsToFirebase(Exception("Error on Sign Profile: $message"))
    }

}