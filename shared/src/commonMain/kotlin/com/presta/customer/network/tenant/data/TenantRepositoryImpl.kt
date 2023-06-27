package com.presta.customer.network.tenant.data

import com.presta.customer.network.tenant.client.PrestaTenantClient
import com.presta.customer.network.tenant.model.PrestaTenantResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TenantRepositoryImpl : TenantRepository,KoinComponent {
    private val tenantClient by inject<PrestaTenantClient>()
    override suspend fun getClientById(searchTerm: String): Result<PrestaTenantResponse> {
        return try {
            val response = tenantClient.getTenantById(
                searchTerm = searchTerm
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}