package com.presta.customer.database.dao

import com.presta.customer.database.PrestaCustomerDatabase
import kotlinx.coroutines.withContext
import prestaDispatchers

class UserAuthDao(
    private val prestaCustomerDatabase: PrestaCustomerDatabase
) {
    // get query
    private val query get() = prestaCustomerDatabase.userAuthEntityQueries

    suspend fun selectUserAuthCredentials() = withContext(prestaDispatchers.io) {
        query.getAccessToken().executeAsList()
    }

    suspend fun removeAccessToken() = withContext(prestaDispatchers.io) {
        query.removeAccessToken()
    }

    suspend fun insert(access_token: String, refresh_token: String, refId: String, registrationFees: Double, registrationFeeStatus: String, phoneNumber: String) = withContext(prestaDispatchers.io) {
        query.insert(
            access_token = access_token,
            refresh_token = refresh_token,
            refId = refId,
            registrationFees = registrationFees,
            registrationFeeStatus = registrationFeeStatus,
            phoneNumber = phoneNumber,
        )
    }
}