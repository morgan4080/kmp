package com.presta.customer.network.loanRequest.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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
data class LoanFee @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val name: String? =null,
    @EncodeDefault val accountNumber: String? =null,
    @EncodeDefault val deferredAccountNumber: String? =null,
    @EncodeDefault val feeType: Feetype? =null,
    @EncodeDefault val feeValue: Double? =null,
    @EncodeDefault val amount: Double? =null,
    @EncodeDefault val balance: Double? =null,
    @EncodeDefault val deductionRule: DeductionRule? =null,
    @EncodeDefault val accountingStatus: AccountingStatus? =null,
    @EncodeDefault val zohoLedgerId: String? =null,
    @EncodeDefault val allocationMethod: AllocationType? =null,
    @EncodeDefault val paymentStatus: LoanPaymentStatus? = null
)








