package com.presta.customer.network.shortTermLoans.model

import com.presta.customer.network.loanRequest.model.Offer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


@Serializable
data class PrestaLoanOfferMaturityResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val offer: Offer?=null

)
