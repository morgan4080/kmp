package com.presta.customer.network.longTermLoans.errors

enum class LongTermLoansError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}
class  LongTermLoansExceptions(error: LongTermLoansError, message: String?): Exception(
    "Data loading  Error: ${if (message !== null) message else error}"
)