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
        query.getUserAuthCredentials().executeAsList()
    }

    suspend fun removeUserAuthCredentials() = withContext(prestaDispatchers.io) {
        query.removeUserAuthCredentials()
    }

    suspend fun updateAccessToken(accessToken: String, refreshToken: String, sessionId: String, refId: String) = withContext(prestaDispatchers.io) {
        query.updateAccessToken(
            access_token = accessToken,
            refresh_token = refreshToken,
            session_id = sessionId,
            refId = refId,
        )
    }

    suspend fun insert(access_token: String, refresh_token: String, refId: String, session_id: String, registrationFees: Double, registrationFeeStatus: String, phoneNumber: String) = withContext(prestaDispatchers.io) {
        query.insert(
            access_token = access_token,
            refresh_token = refresh_token,
            refId = refId,
            session_id = session_id,
            registrationFees = registrationFees,
            registrationFeeStatus = registrationFeeStatus,
            phoneNumber = phoneNumber,
        )
    }
}