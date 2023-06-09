package com.presta.customer.network.onBoarding.errors

enum class OnBoardingError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class OnBoardingException(error: OnBoardingError, message: String?): Exception(
    "Account Config Error: ${if (message !== null) message else error}"
)