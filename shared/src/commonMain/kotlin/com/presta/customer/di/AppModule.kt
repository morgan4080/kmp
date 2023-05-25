package com.presta.customer.di

import com.presta.customer.database.di.databaseModule
import com.presta.customer.network.di.dataModule
import com.presta.customer.network.di.networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(enableNetworkLogs: Boolean = false, appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            databaseModule,
            networkModule(enableNetworkLogs),
            dataModule
        )
    }