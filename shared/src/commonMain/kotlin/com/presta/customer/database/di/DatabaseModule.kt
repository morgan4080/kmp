package com.presta.customer.database.di
import com.presta.customer.database.createDatabase
import com.presta.customer.database.dao.UserAuthDao
import com.presta.customer.database.sqlDriverFactory
import org.koin.dsl.module

val databaseModule = module {
    factory { sqlDriverFactory() }
    single { createDatabase(driver = get()) }
    single { UserAuthDao(prestaCustomerDatabase = get()) }
}