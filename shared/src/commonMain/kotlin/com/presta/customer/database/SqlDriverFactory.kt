package com.presta.customer.database

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.scope.Scope

fun createDatabase(driver: SqlDriver): PrestaCustomerDatabase {
    val database = PrestaCustomerDatabase(
        driver = driver,
    )

    return database
}
expect fun Scope.sqlDriverFactory(): SqlDriver