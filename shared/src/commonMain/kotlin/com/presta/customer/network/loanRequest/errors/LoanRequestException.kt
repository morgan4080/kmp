package com.presta.customer.network.loanRequest.errors

enum class LoanRequestError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class LoanRequestExceptions(error: LoanRequestError ): Exception(
    "Loan Request  Error: $error"
)