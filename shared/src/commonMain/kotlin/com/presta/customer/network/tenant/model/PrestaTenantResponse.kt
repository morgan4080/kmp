package com.presta.customer.network.tenant.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


@Serializable
data class PrestaTenantResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val tenantId: String? = null,
    @EncodeDefault val alias: String? = null,
)
