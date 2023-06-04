package com.presta.customer.network.payments.data

import com.presta.customer.network.payments.client.PrestaPaymentsClient
import com.presta.customer.network.payments.model.PaymentsResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PaymentsRepositoryImpl : PaymentsRepository, KoinComponent {
    private val paymentsClient by inject<PrestaPaymentsClient>()
    override suspend fun makePayment(
        token: String,
        phoneNumber: String,
        loanRefId: String?,
        beneficiaryPhoneNumber: String?,
        amount: Int,
        paymentType: PaymentTypes
    ): Result<PaymentsResponse> {
        return try {
            val response = paymentsClient.makeC2BPayment(
                token = token,
                phoneNumber = phoneNumber,
                loanRefId = loanRefId,
                beneficiaryPhoneNumber = beneficiaryPhoneNumber,
                amount = amount,
                paymentType = paymentType
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}