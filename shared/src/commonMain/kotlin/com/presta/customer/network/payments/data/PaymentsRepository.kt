package com.presta.customer.network.payments.data

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentTypes {
    INVESTMENT,
    LOAN,
    SHARES,
    DEPOSITS,
    SAVINGS,
    MEMBERSHIPFEES
}
interface PaymentsRepository {
    suspend fun pollPaymentStatus(
        token: String,
        correlationId: String
    ): Result<String>
    suspend fun makePayment(
        token: String,
        phoneNumber: String,
        loanRefId: String?,
        beneficiaryPhoneNumber: String?,
        amount: Int,
        paymentType: PaymentTypes
    ): Result<String>
}