package com.presta.customer.ui.components.signGuarantorForm.poller

import com.presta.customer.network.longTermLoans.data.LongTermLoansRepository
import com.presta.customer.network.longTermLoans.model.guarantorResponse.PrestaGuarantorResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn

class GuarantorSigningStatusPoller(
    private val dataRepository: LongTermLoansRepository,
    private val dispatcher: CoroutineDispatcher
) : SignGuarantorFormPoller {
    @OptIn(DelicateCoroutinesApi::class)
    override fun poll(
        delay: Long,
        token: String,
        guarantorRefId: String
    ): Flow<Result<List<PrestaGuarantorResponse>>> {
        return channelFlow {
            while (!isClosedForSend) {
                val data = dataRepository.getGuarantorshipRequests(token, guarantorRefId)
                send(data)
                delay(delay)
            }
        }.flowOn(dispatcher)
    }

    override fun close() {
        dispatcher.cancel()
    }
}