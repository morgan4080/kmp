package com.presta.customer.network.tenant.model

import kotlinx.serialization.Serializable


@Serializable
data class PrestaTenantResponse (
    val tenantId: String,
    val alias: String,
)
