package network.otp.errors

enum class OtpError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class OtpException(error: OtpError): Exception(
    "On-Boarding Error: $error"
)