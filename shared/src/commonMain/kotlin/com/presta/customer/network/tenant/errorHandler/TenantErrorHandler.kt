package com.presta.customer.network.tenant.errorHandler

import com.presta.customer.network.authDevice.errorHandler.Message
import com.presta.customer.network.tenant.errors.TenantExceptions
import com.presta.customer.network.tenant.errors.TenantsError
import com.presta.customer.prestaDispatchers
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext

suspend inline fun <reified T> tenantErrorHandler(
    crossinline response: suspend () -> HttpResponse
): T = withContext(prestaDispatchers.io) {
    val result = try {
        response()
    } catch(e: IOException) {
        throw TenantExceptions(TenantsError.ServiceUnavailable, null)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw TenantExceptions(TenantsError.ClientError, null)
        500 -> {
            val data: Message = result.body()
            throw TenantExceptions(TenantsError.ServerError, data.message)
        }
        else -> throw TenantExceptions(TenantsError.UnknownError, null)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw TenantExceptions(TenantsError.ServerError, null)
    }

}