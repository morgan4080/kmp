package com.presta.customer.di
import com.presta.customer.network.onBoarding.client.PrestaOnBoardingClient
import com.presta.customer.network.authDevice.client.PrestaAuthClient
import com.presta.customer.network.createHttpClient
import com.presta.customer.network.otp.client.PrestaOtpClient
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: (enableLogging: Boolean) -> Module get() = { enableLogging ->
    module {
        single { createHttpClient(enableLogging) }
        single { PrestaAuthClient(httpClient = get()) }
        single { PrestaOnBoardingClient(httpClient = get()) }
        single { PrestaOtpClient(httpClient = get()) }
    }
}