package com.presta.customer.network.longTermLoans.errors

import com.presta.customer.Platform
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

enum class LongTermLoansError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}
class  LongTermLoansExceptions(error: LongTermLoansError, message: String?): Exception(
    "Data loading  Error: ${message ?: error}"
),KoinComponent{
    val platform by inject<Platform>()
    init {
        if (message !== null) platform.logErrorsToFirebase(Exception("Error on LongTerm Loans: $message"))
    }

}