package com.presta.customer.network.longTermLoans.model

import com.presta.customer.network.longTermLoans.client.DetailsData
import kotlinx.serialization.Serializable

@Serializable
data class LongTermLoanRequestResponse constructor(
    val details: DetailsData,
    val loanProductName: String,
    val loanProductRefId: String,
    val selfCommitment: Double,
    val loanAmount: Double,
    val memberRefId: String,
    val memberNumber: String,
    val witnessRefId: String?,
    val guarantorList: List<Guarantor>,
)
