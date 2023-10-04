package com.presta.customer.network

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.ui.components.root.DefaultRootComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun createHttpClient(enableLogging: Boolean, componentContext: ComponentContext, storeFactory: StoreFactory): HttpClient {

    val client = createPlatformHttpClient().config {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }

        if (enableLogging) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    client.plugin(HttpSend).intercept { request ->
        val originalCall = execute(request)
        if (originalCall.response.status.value !in 100..399) {
            execute(request)
        } else {
            originalCall
        }
    }

    return client
}