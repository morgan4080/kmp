package network.di
import components.onBoarding.network.client.PrestaOnBoardingClient
import network.client.PrestaAuthClient
import network.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: (enableLogging: Boolean) -> Module get() = { enableLogging ->
    module {
        single { createHttpClient(enableLogging) }
        single { PrestaAuthClient(httpClient = get()) }
        single { PrestaOnBoardingClient(httpClient = get()) }
    }
}