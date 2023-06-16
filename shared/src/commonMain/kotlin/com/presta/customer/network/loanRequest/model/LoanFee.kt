package com.presta.customer.network.loanRequest.model

import kotlinx.serialization.Serializable

enum class Feetype {
    RATE,
    FIXED_AMOUNT,
    FIXED_AMOUNT_PER_INSTALLMENT
}


enum class DeductionRule {
    DEDUCT_FROM_DISBURSEMENT, //Deducted Fees
    DO_NOT_DEDUCT, //Post Paid Fees
    UPFRONT_FEES, //Upfront Fees
    NONE
}


enum class AllocationType {
    SPREAD,CLEAR
}


enum class AccountingStatus {
    NOTACCOUNTED, ACCOUNTED
}

enum class LoanPaymentStatus {
    PAID,
    PARTIALLYPAID,
    BRIDGED,
    NOTPAID
}

@Serializable
data class LoanFee (
    val name: String,
    val accountNumber: String,
    val deferredAccountNumber: String,
    val feeType: Feetype,
    val feeValue: Double,
    val amount: Double,
    val balance: Double,
    val deductionRule: DeductionRule,
    val accountingStatus: AccountingStatus,
    val zohoLedgerId: String,
    val allocationMethod: AllocationType,
    val paymentStatus: LoanPaymentStatus
)








