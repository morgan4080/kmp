package di

import network.authDevice.data.AuthRepository
import network.authDevice.data.AuthRepositoryImpl
import network.onBoarding.data.OnBoardingRepository
import network.onBoarding.data.OnBoardingRepositoryImpl
import network.otp.data.OtpRepository
import network.otp.data.OtpRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<OnBoardingRepository> { OnBoardingRepositoryImpl() }
    single<OtpRepository> { OtpRepositoryImpl() }
}