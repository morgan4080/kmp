package com.presta.customer.network.tenant.errors


enum class TenantsError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}
class TenantExceptions(error: TenantsError, message: String?): Exception(
    "Data loading  Error: ${if (message !== null) message else error}"
)