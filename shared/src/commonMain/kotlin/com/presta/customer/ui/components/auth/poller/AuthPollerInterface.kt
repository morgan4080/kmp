package com.presta.customer.ui.components.auth.poller

import com.presta.customer.network.authDevice.model.RefreshTokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthPollerInterface {
    fun poll(delay: Long,tenantId: String, refId: String): Flow<Result<RefreshTokenResponse>>
    fun close()
}