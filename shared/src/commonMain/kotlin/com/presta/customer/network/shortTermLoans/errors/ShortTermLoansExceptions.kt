package com.presta.customer.network.shortTermLoans.errors

enum class ShortTermLoansError  {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class ShortTermLoansExceptions(error: ShortTermLoansError): Exception(
    "Data loading  Error: $error"
)