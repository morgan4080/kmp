package com.presta.customer.database.dao

import androidx.compose.foundation.layout.RowScope
import com.presta.customer.database.PrestaCustomerDatabase
import kotlinx.coroutines.withContext
import com.presta.customer.prestaDispatchers

class UserAuthDao(
    private val prestaCustomerDatabase: PrestaCustomerDatabase
) {
    // get query
    private val query get() = prestaCustomerDatabase.userAuthEntityQueries

    suspend fun selectUserAuthCredentials() = withContext(prestaDispatchers.io) {
        try {
            query.getUserAuthCredentials().executeAsList()
        } catch (e: Exception) {
            e.printStackTrace()

            listOf()
        }
    }

    suspend fun removeUserAuthCredentials() = withContext(prestaDispatchers.io) {
        query.removeUserAuthCredentials()
    }

    suspend fun updateAccessToken(accessToken: String, refreshToken: String, sessionId: String, expires_in: Long, refresh_expires_in: Long, refId: String, tenant_id: String) = withContext(
        prestaDispatchers.io
    ) {
        query.updateAccessToken(
            access_token = accessToken,
            refresh_token = refreshToken,
            session_id = sessionId,
            refId = refId,
            expires_in = expires_in,
            refresh_expires_in = refresh_expires_in,
            tenant_id = tenant_id
        )
    }

    suspend fun insert(access_token: String, refresh_token: String, refId: String, session_id: String, registrationFees: Double, registrationFeeStatus: String, phoneNumber: String, expires_in: Long, refresh_expires_in: Long, tenant_id: String) = withContext(
        prestaDispatchers.io
    ) {
        query.insert(
            access_token = access_token,
            refresh_token = refresh_token,
            refId = refId,
            session_id = session_id,
            registrationFees = registrationFees,
            registrationFeeStatus = registrationFeeStatus,
            phoneNumber = phoneNumber,
            expires_in = expires_in,
            refresh_expires_in = refresh_expires_in,
            tenant_id = tenant_id
        )
    }
}