package com.presta.customer.network.profile.errors

enum class ProfileError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class ProfileException(error: ProfileError): Exception(
    "Data loading  Error: $error"
)