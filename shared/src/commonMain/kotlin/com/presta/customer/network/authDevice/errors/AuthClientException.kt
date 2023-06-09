package com.presta.customer.network.authDevice.errors

enum class AuthClientError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class AuthClientException(error: AuthClientError, message: String?): Exception(
    "Auth Error: ${if (message !== null) message else error}"
)