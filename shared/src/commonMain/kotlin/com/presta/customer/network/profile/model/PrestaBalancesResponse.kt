package com.presta.customer.network.profile.model

import kotlinx.serialization.Serializable

// {"savingsBalance":0.00,"sharesBalance":0.00,"sharesCount":0,"savingsTotalAmount":0.00,"pricePerShare":"100","lastSavingsAmount":0.00,"lastSavingsDate":"0 days ago"}
@Serializable
data class PrestaSavingsBalancesResponse (
    val savingsBalance:Double,
    val sharesBalance: Double,
    val sharesCount: Int,
    val savingsTotalAmount: Double,
    val pricePerShare: String,
    val lastSavingsAmount: Double,
    val lastSavingsDate: String
)

@Serializable
data class PrestaLoansBalancesResponse (
    val totalBalance:Double,
    val loanCount: Int,
    val loanStatus: String,
    val loanBreakDown: List<LoanBreakDown>,
)

@Serializable
data class LoanBreakDown (
    val refId: String,
    val totalBalance: Double,
    val loanType: String,
    val loanStatus: String,
    val amountDue: Double,
    val requestedAmount: Double,
    val interestRate: Double,
    val interestAmount: Double,
    val loanPeriod: String,
    val dueDate: String,
    val balanceBF: Double,
    val repaymentAmount: Double,
    val disbursedAmount: Double,
    val loanSchedule: List<LoanSchedule>
)

@Serializable
data class LoanSchedule (
    val scheduleDate: String,
    val balance: Double,
    val status: String
)
