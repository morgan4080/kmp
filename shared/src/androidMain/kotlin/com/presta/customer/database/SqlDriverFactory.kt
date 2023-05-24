package com.presta.customer.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import com.presta.customer.database.PrestaCustomerDatabase

actual fun Scope.sqlDriverFactory(): SqlDriver {
    return AndroidSqliteDriver(PrestaCustomerDatabase.Schema, androidContext(), "${DatabaseConstants.name}.db")
}