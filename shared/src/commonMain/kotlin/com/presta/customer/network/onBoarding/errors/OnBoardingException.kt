package com.presta.customer.network.onBoarding.errors

enum class OnBoardingError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class OnBoardingException(error: OnBoardingError, message: String?): Exception(
    "Service Unavailable, try again in a few minutes: ${message ?: error}"
)