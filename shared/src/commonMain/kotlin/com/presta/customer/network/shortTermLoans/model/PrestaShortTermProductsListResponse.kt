package com.presta.customer.network.shortTermLoans.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaShortTermProductsListResponse(
    val daysAvailable: String,
    val interestPeriodCycle: String,
    val interestRate: Double,
    val isInterestGraduated: Boolean,
    val loanPeriodUnit: String,
    val maxAmount: Double,
    val maxTerm: Int,
    val minAmount: Double,
    val minTerm: Int,
    val name: String,
    val refId: String
)