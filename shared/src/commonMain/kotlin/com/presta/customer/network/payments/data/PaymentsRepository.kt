package com.presta.customer.network.payments.data

import com.presta.customer.network.payments.model.PaymentsResponse
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
    suspend fun makePayment(
        token: String,
        phoneNumber: String,
        loanRefId: String?,
        beneficiaryPhoneNumber: String?,
        amount: Int,
        paymentType: PaymentTypes
    ): Result<PaymentsResponse>
}