package com.presta.customer.database.dao

import kotlinx.coroutines.withContext
import com.presta.customer.database.PrestaCustomerDatabase
import comprestacustomer.UserAuthEntity
import prestaDispatchers

class UserAuthDao(
    private val prestaCustomerDatabase: PrestaCustomerDatabase
) {
    // get query
    private val query get() = prestaCustomerDatabase.userAuthEntityQueries

    suspend fun selectUserAuthCredentials() = withContext(prestaDispatchers.io) {
        query.getAccessToken().executeAsOne()
    }

    suspend fun insert(userAuthEntity: UserAuthEntity) = withContext(prestaDispatchers.io) {
        query.insert(userAuthEntity)
    }
}