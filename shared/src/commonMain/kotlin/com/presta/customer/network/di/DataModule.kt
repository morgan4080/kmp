package com.presta.customer.network.di

import com.presta.customer.network.authDevice.data.AuthRepository
import com.presta.customer.network.authDevice.data.AuthRepositoryImpl
import com.presta.customer.network.loanRequest.data.LoanRequestRepository
import com.presta.customer.network.loanRequest.data.LoanRequestRepositoryImpl
import com.presta.customer.network.longTermLoans.data.LongTermLoansRepository
import com.presta.customer.network.longTermLoans.data.LongTermLoansRepositoryImpl
import com.presta.customer.network.onBoarding.data.OnBoardingRepository
import com.presta.customer.network.onBoarding.data.OnBoardingRepositoryImpl
import com.presta.customer.network.otp.data.OtpRepository
import com.presta.customer.network.otp.data.OtpRepositoryImpl
import com.presta.customer.network.payments.data.PaymentsRepository
import com.presta.customer.network.payments.data.PaymentsRepositoryImpl
import com.presta.customer.network.profile.data.ProfileRepository
import com.presta.customer.network.profile.data.ProfileRepositoryImpl
import com.presta.customer.network.registration.data.RegistrationRepository
import com.presta.customer.network.registration.data.RegistrationRepositoryImpl
import com.presta.customer.network.shortTermLoans.data.ShortTermLoansRepository
import com.presta.customer.network.shortTermLoans.data.ShortTermLoansRepositoryImpl
import com.presta.customer.network.signHome.data.SignHomeRepository
import com.presta.customer.network.signHome.data.SignHomeRepositoryImpl
import com.presta.customer.network.tenant.data.TenantRepository
import com.presta.customer.network.tenant.data.TenantRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<OnBoardingRepository> { OnBoardingRepositoryImpl() }
    single<OtpRepository> { OtpRepositoryImpl() }
    single<RegistrationRepository> { RegistrationRepositoryImpl() }
    single<ProfileRepository> { ProfileRepositoryImpl() }
    single<PaymentsRepository> { PaymentsRepositoryImpl() }
    single<ShortTermLoansRepository> { ShortTermLoansRepositoryImpl() }
    single<LoanRequestRepository>{ LoanRequestRepositoryImpl() }
    single<TenantRepository>{ TenantRepositoryImpl()}
    single<SignHomeRepository>{ SignHomeRepositoryImpl()}
    single<LongTermLoansRepository>{ LongTermLoansRepositoryImpl()}
}