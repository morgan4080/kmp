package com.presta.customer.ui.components.processLoanDisbursement.poller

import com.presta.customer.network.loanRequest.model.PrestaLoanPollingResponse
import kotlinx.coroutines.flow.Flow

interface Poller {
    fun poll(delay: Long, token: String, requestId: String): Flow<Result<PrestaLoanPollingResponse>>
    fun close()
}