package com.presta.customer.network.profile.errors

enum class ProfileError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class ProfileException(error: ProfileError, message: String?): Exception(
    "Data loading  Error: ${if (message !== null) message else error}"
)