package com.presta.customer.network.di
import com.presta.customer.network.onBoarding.client.PrestaOnBoardingClient
import com.presta.customer.network.authDevice.client.PrestaAuthClient
import com.presta.customer.network.createHttpClient
import com.presta.customer.network.otp.client.PrestaOtpClient
import com.presta.customer.network.profile.client.PrestaProfileClient
import com.presta.customer.network.registration.client.PrestaRegistrationClient
import com.presta.customer.network.shortTermLoans.client.PrestaShortTermLoansClient
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: (enableLogging: Boolean) -> Module get() = { enableLogging ->
    module {
        single { createHttpClient(enableLogging) }
        single { PrestaAuthClient(httpClient = get()) }
        single { PrestaOnBoardingClient(httpClient = get()) }
        single { PrestaOtpClient(httpClient = get()) }
        single { PrestaProfileClient(httpClient = get()) }
        single { PrestaRegistrationClient(httpClient = get()) }
        single { PrestaShortTermLoansClient(httpClient = get()) }

    }
}