package com.presta.customer.network.registration.data

import com.presta.customer.network.registration.model.PrestaRegistrationResponse

interface RegistrationRepository {
    suspend fun createMember(
        token: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        idNumber: String,
        tocsAccepted: Boolean,
        tenantId: String,
    ): Result<PrestaRegistrationResponse>
}