package com.presta.customer.network.tenant.data

import com.presta.customer.network.tenant.model.PrestaTenantResponse

interface TenantRepository {
    suspend fun getClientById(searchTerm:String): Result<PrestaTenantResponse>
}