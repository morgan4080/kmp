package di

import network.authDevice.data.AuthRepository
import network.authDevice.data.AuthRepositoryImpl
import network.onBoarding.data.OnBoardingRepository
import network.onBoarding.data.OnBoardingRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    module {
        single<AuthRepository> { AuthRepositoryImpl() }
        single<OnBoardingRepository> { OnBoardingRepositoryImpl() }
    }
}