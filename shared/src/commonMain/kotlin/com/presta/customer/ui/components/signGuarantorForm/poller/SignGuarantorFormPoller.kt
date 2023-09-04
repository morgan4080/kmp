package com.presta.customer.ui.components.signGuarantorForm.poller

import com.presta.customer.network.longTermLoans.model.guarantorResponse.PrestaGuarantorResponse
import kotlinx.coroutines.flow.Flow

interface SignGuarantorFormPoller {
    fun poll(
        delay: Long,
        token: String,
        guarantorRefId: String
    ): Flow<Result<List<PrestaGuarantorResponse>>>

    fun close()

}