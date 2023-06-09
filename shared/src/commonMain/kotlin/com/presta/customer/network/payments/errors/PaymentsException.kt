package com.presta.customer.network.payments.errors

enum class PaymentsError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class PaymentsException(error: PaymentsError): Exception(
    "Payments Error: $error"
)