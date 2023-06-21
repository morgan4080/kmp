package com.presta.customer.network.shortTermLoans.errors

enum class ShortTermLoansError  {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class ShortTermLoansExceptions(error: ShortTermLoansError, message: String?): Exception(
    "Data loading  Error: ${if (message !== null) message else error}"
)