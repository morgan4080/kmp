package com.presta.customer.ui.components.signLoanForm.poller

import com.presta.customer.network.longTermLoans.model.PrestaLoanRequestByRequestRefId
import kotlinx.coroutines.flow.Flow

interface SignLoanFormPoller {
    fun poll(
        delay: Long,
        token: String,
        loanRequestRefId: String
    ): Flow<Result<PrestaLoanRequestByRequestRefId>>

    fun close()

}