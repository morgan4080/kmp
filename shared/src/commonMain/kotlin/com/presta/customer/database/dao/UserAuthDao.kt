package com.presta.customer.database.dao

import com.presta.customer.database.PrestaCustomerDB
import com.presta.customer.prestaDispatchers
import comprestacustomer.UserAuthEntity
import kotlinx.coroutines.withContext

class UserAuthDao(
    private val prestaCustomerDatabase: PrestaCustomerDB
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

    suspend fun updateMetaData(keycloakId: String, username: String, email: String?, firstName: String, lastName: String, refId: String) = withContext(
        prestaDispatchers.io
    ) {
        query.updateMetaData(
            keycloakId = keycloakId,
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName,
            refId = refId,
        )
    }

    suspend fun insert(access_token: String, refresh_token: String, refId: String, session_id: String, registrationFees: Double, registrationFeeStatus: String, phoneNumber: String, expires_in: Long, refresh_expires_in: Long, tenant_id: String) = withContext(
        prestaDispatchers.io
    ) {
        val data = UserAuthEntity(
            access_token = access_token,
            refresh_token = refresh_token,
            refId = refId,
            session_id = session_id,
            registrationFees = registrationFees,
            registrationFeeStatus = registrationFeeStatus,
            phoneNumber = phoneNumber,
            expires_in = expires_in,
            refresh_expires_in = refresh_expires_in,
            tenant_id = tenant_id,
            keycloakId = null,
            username = null,
            email = null,
            firstName = null,
            lastName = null
        )

        query.insertOrReplaceFullObject(data)
    }
}