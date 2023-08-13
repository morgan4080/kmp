package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaLongTermLoansRequestsListResponse(
    val content: List<LoanRequestListData>
)
