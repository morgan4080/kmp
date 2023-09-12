package com.presta.customer.database

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.scope.Scope

fun createDatabase(driver: SqlDriver): PrestaCustomerDB {
    return PrestaCustomerDB(
        driver = driver,
    )
}
expect fun Scope.sqlDriverFactory(): SqlDriver