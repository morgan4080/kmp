package com.presta.customer.ui.components.signWitnessForm.poller

import com.presta.customer.network.longTermLoans.data.LongTermLoansRepository
import com.presta.customer.network.longTermLoans.model.guarantorResponse.PrestaGuarantorResponse
import com.presta.customer.network.longTermLoans.model.witnessRequests.PrestaWitnessRequestResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn

class WitnessSigningStatusPoller(
    private val dataRepository: LongTermLoansRepository,
    private val dispatcher: CoroutineDispatcher
) : SignWitnessFormPoller {
    @OptIn(DelicateCoroutinesApi::class)
    override fun poll(
        delay: Long,
        token: String,
        guarantorRefId: String
    ): Flow<Result<List<PrestaWitnessRequestResponse>>> {
        return channelFlow {
            while (!isClosedForSend) {
                val data = dataRepository.getWitnessRequests(token, guarantorRefId)
                send(data)
                delay(delay)
            }
        }.flowOn(dispatcher)
    }

    override fun close() {
        dispatcher.cancel()
    }
}