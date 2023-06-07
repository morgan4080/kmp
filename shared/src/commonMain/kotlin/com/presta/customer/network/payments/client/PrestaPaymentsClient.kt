package com.presta.customer.network.payments.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.network.payments.errorHandler.paymentsErrorHandler
import com.presta.customer.network.payments.model.PrestaPollingResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class PrestaPaymentsClient(
    private val httpClient: HttpClient
) {

    suspend fun pollPaymentStatus(
        token: String,
        correlationId: String
    ): PrestaPollingResponse {
        return paymentsErrorHandler {
            httpClient.get(NetworkConstants.PrestaPollPaymentStatus.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)

                url {
                    parameters.append("correlationId", correlationId)
                }
            }
        }
    }
    suspend fun makeC2BPayment(
        token: String,
        phoneNumber: String,
        loanRefId: String?,
        beneficiaryPhoneNumber: String?,
        amount: Int,
        paymentType: PaymentTypes
    ): String {
        return paymentsErrorHandler {
            httpClient.post(NetworkConstants.PrestaMakePayment.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    C2BPaymentData(
                        phoneNumber = phoneNumber,
                        loanRefId = loanRefId,
                        beneficiaryPhoneNumber = beneficiaryPhoneNumber,
                        amount = amount,
                        paymentType = paymentType
                    )
                )
            }
        }
    }
}
@Serializable
data class C2BPaymentData(
    val phoneNumber: String,
    val loanRefId: String?,
    val beneficiaryPhoneNumber: String?,
    val amount: Int,
    val paymentType: PaymentTypes
)
