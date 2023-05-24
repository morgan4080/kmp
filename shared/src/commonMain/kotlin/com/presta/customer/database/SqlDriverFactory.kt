package com.presta.customer.database

import com.presta.customer.database.PrestaCustomerDatabase
import app.cash.sqldelight.db.SqlDriver
import org.koin.core.scope.Scope

expect fun Scope.sqlDriverFactory(): SqlDriver

fun createDatabase(driver: SqlDriver): PrestaCustomerDatabase {
    val database = PrestaCustomerDatabase(
        driver = driver,
    )

    return database
}