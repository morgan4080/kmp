package network.authDevice.errors

enum class AuthClientError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class AuthClientException(error: AuthClientError): Exception(
    "Client Authentication Error: $error"
)