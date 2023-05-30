package com.presta.customer.network.registration.errors

enum class RegistrationError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class RegistrationException(error: RegistrationError, message: String?): Exception(
    "Registration Error: ${if (message !== null) message else error}"
)