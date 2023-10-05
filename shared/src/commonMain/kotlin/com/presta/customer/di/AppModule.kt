package com.presta.customer.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.Platform
import com.presta.customer.database.di.databaseModule
import com.presta.customer.network.di.dataModule
import com.presta.customer.network.di.networkModule
import com.presta.customer.ui.components.di.componentModule
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.components.root.RootComponent
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(componentContext: ComponentContext, storeFactory: StoreFactory, enableNetworkLogs: Boolean = false, appDeclaration: KoinAppDeclaration = {}, platform: Platform?) = startKoin {
    appDeclaration()
    modules(
        databaseModule,
        networkModule(enableNetworkLogs, componentContext, storeFactory, platform),
        dataModule,
        componentModule,
        module {
            factory {  platform }
        }
    )
}