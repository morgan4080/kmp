package network.errors

enum class AuthClientError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class AuthClientException(error: AuthClientError): Exception(
    "Client Authentication Error: $error"
)