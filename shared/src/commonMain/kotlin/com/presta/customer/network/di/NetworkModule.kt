package com.presta.customer.network.di
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.Platform
import com.presta.customer.network.onBoarding.client.PrestaOnBoardingClient
import com.presta.customer.network.authDevice.client.PrestaAuthClient
import com.presta.customer.network.createHttpClient
import com.presta.customer.network.loanRequest.client.PrestaLoanRequestClient
import com.presta.customer.network.longTermLoans.client.PrestaLongTermLoansClient
import com.presta.customer.network.otp.client.PrestaOtpClient
import com.presta.customer.network.payments.client.PrestaPaymentsClient
import com.presta.customer.network.profile.client.PrestaProfileClient
import com.presta.customer.network.registration.client.PrestaRegistrationClient
import com.presta.customer.network.shortTermLoans.client.PrestaShortTermLoansClient
import com.presta.customer.network.signHome.client.PrestaSignHomeClient
import com.presta.customer.network.tenant.client.PrestaTenantClient
import com.presta.customer.ui.components.root.DefaultRootComponent
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: (enableLogging: Boolean, componentContext: ComponentContext, storeFactory: StoreFactory, platform: Platform?) -> Module get() = { enableLogging, componentContext, storeFactory, platform ->
    module {
        single { createHttpClient(enableLogging, componentContext, storeFactory, platform) }
        single { PrestaAuthClient(httpClient = get()) }
        single { PrestaOnBoardingClient(httpClient = get()) }
        single { PrestaOtpClient(httpClient = get()) }
        single { PrestaProfileClient(httpClient = get()) }
        single { PrestaRegistrationClient(httpClient = get()) }
        single { PrestaShortTermLoansClient(httpClient = get()) }
        single { PrestaPaymentsClient(httpClient = get()) }
        single { PrestaLoanRequestClient(httpClient = get()) }
        single { PrestaTenantClient(httpClient = get()) }
        single { PrestaSignHomeClient(httpClient = get()) }
        single { PrestaLongTermLoansClient(httpClient = get()) }
    }
}