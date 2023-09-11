package com.presta.customer.network.signHome.errors

enum class SignHomeError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}
class SignHomeException(error: SignHomeError, message: String?): Exception(
    "Data loading  Error: ${if (message !== null) message else error}"
)