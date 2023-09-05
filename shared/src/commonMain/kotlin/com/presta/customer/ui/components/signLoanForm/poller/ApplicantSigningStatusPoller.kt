package com.presta.customer.ui.components.signLoanForm.poller

import com.presta.customer.network.longTermLoans.data.LongTermLoansRepository
import com.presta.customer.network.longTermLoans.model.PrestaLoanRequestByRequestRefId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn

class ApplicantSigningStatusPoller(
    private val dataRepository: LongTermLoansRepository,
    private val dispatcher: CoroutineDispatcher
) : SignLoanFormPoller {
    @OptIn(DelicateCoroutinesApi::class)
    override fun poll(
        delay: Long,
        token: String,
        loanRequestRefId: String
    ): Flow<Result<PrestaLoanRequestByRequestRefId>>{
        return channelFlow {
            while (!isClosedForSend) {
                val data = dataRepository.getLoansByLoanRequestRefId(token,  loanRequestRefId)
                send(data)
                delay(delay)
            }
        }.flowOn(dispatcher)
    }
    override fun close() {
        dispatcher.cancel()
    }
}