package com.presta.customer.network.onBoarding.errors

enum class OnBoardingError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class OnBoardingException(error: OnBoardingError): Exception(
    "On-Boarding Error: $error"
)