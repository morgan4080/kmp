package com.presta.customer.ui.components.signWitnessForm.poller

import com.presta.customer.network.longTermLoans.model.witnessRequests.PrestaWitnessRequestResponse
import kotlinx.coroutines.flow.Flow

interface SignWitnessFormPoller {
    fun poll(
        delay: Long,
        token: String,
        guarantorRefId: String
    ): Flow<Result<List<PrestaWitnessRequestResponse>>>

    fun close()

}