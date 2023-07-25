package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.Serializable


@Serializable
data class PrestaLongTermLoanCategoriesResponse(
    val code: String,
    val name: String
)