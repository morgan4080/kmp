package com.presta.customer.network.registration.errors

enum class RegistrationError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class RegistrationException(error: RegistrationError): Exception(
    "Registration Error: $error"
)