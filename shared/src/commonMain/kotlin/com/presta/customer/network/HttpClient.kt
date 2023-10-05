package com.presta.customer.network

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.Platform
import com.presta.customer.network.authDevice.errorHandler.Message
import com.presta.customer.ui.components.root.DefaultRootComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Message(val message: String, val code: Int, val timestamp: String)
internal fun createHttpClient(enableLogging: Boolean, componentContext: ComponentContext, storeFactory: StoreFactory, platform: Platform?): HttpClient {

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
            val data: Message = originalCall.response.body()
            if (platform !== null) {
                platform.logErrorsToFirebase(Exception("${data.timestamp} - STATUS: ${originalCall.response.status}: ${data.message}"))
                platform.networkError.value = true
            }
            execute(request)
        } else {
            originalCall
        }
    }

    return client
}