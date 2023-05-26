package com.presta.customer.network.registration.data

import com.presta.customer.network.registration.client.PrestaRegistrationClient
import com.presta.customer.network.registration.model.PrestaRegistrationResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegistrationRepositoryImpl: RegistrationRepository, KoinComponent {
    private val registrationClient by inject<PrestaRegistrationClient>()

    override suspend fun createMember(
        token: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        idNumber: String,
        tocsAccepted: Boolean,
        tenantId: String,
    ): Result<PrestaRegistrationResponse> {
        return try {
            val response = registrationClient.createMember(
                token = token,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                idNumber = idNumber,
                tocsAccepted = tocsAccepted,
                tenantId = tenantId
            )

            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}