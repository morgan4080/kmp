package components.onBoarding.network.errors

enum class OnBoardingError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class OnBoardingException(error: OnBoardingError): Exception(
    "Something goes wrong: $error"
)