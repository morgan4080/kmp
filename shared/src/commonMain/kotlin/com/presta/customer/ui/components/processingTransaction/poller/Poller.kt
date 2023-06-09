package com.presta.customer.ui.components.processingTransaction.poller

import com.presta.customer.network.payments.model.PrestaPollingResponse
import kotlinx.coroutines.flow.Flow

interface Poller {
    fun poll(delay: Long, token: String, correlationId: String): Flow<Result<PrestaPollingResponse>>
    fun close()
}