package com.presta.customer.network.loanRequest.errors

enum class LoanRequestError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class LoanRequestExceptions(error: LoanRequestError, message: String?): Exception(
    "Loan Request  Error: ${if (message !== null) message else error}"
)