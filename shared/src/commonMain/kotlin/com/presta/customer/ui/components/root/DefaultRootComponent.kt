package com.presta.customer.ui.components.root

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.ui.components.auth.AuthComponent
import com.presta.customer.ui.components.auth.DefaultAuthComponent
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.onBoarding.DefaultOnboardingComponent
import com.presta.customer.ui.components.onBoarding.OnBoardingComponent
import com.presta.customer.ui.components.otp.DefaultOtpComponent
import com.presta.customer.ui.components.otp.OtpComponent
import com.presta.customer.ui.components.rootBottomStack.DefaultRootBottomComponent
import com.presta.customer.ui.components.rootBottomStack.RootBottomComponent
import com.presta.customer.ui.components.splash.DefaultSplashComponent
import com.presta.customer.ui.components.splash.SplashComponent
import com.presta.customer.ui.components.welcome.DefaultWelcomeComponent
import com.presta.customer.ui.components.welcome.WelcomeComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow


class DefaultRootComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
) : RootComponent, ComponentContext by componentContext {
    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    init {
        authStore.accept(AuthStore.Intent.GetCachedToken)
    }

    private val navigation = StackNavigation<Config>()

    private val _childStack =
        childStack(
            source = navigation,
            initialConfiguration = Config.Splash,
            handleBackButton = true,
            childFactory = ::createChild,
            key = "onboardingStack"
        )

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = _childStack

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Splash -> RootComponent.Child.SplashChild(splashComponent(componentContext))
            is Config.Welcome -> RootComponent.Child.WelcomeChild(welcomeComponent(componentContext, config))
            is Config.OnBoarding -> RootComponent.Child.OnboardingChild(onboardingComponent(componentContext, config))
            is Config.OTP -> RootComponent.Child.OTPChild(otpComponent(componentContext, config))
            is Config.Auth -> RootComponent.Child.AuthChild(authComponent(componentContext, config))
            is Config.RootBottom -> RootComponent.Child.RootBottomChild(rootBottomComponent(componentContext))
        }

    private fun splashComponent(componentContext: ComponentContext): SplashComponent =
        DefaultSplashComponent(
            componentContext = componentContext,
            onSignUp = {
                navigation.push(Config.Welcome(context = OnBoardingContext.REGISTRATION))
            },
            onSignIn = {
                navigation.push(Config.Welcome(context = OnBoardingContext.LOGIN))
            },
        )

    private fun welcomeComponent(componentContext: ComponentContext, config: Config.Welcome): WelcomeComponent =
        DefaultWelcomeComponent(
            componentContext = componentContext,
            onBoardingContext = config.context,
            onGetStartedSelected = {
                navigation.push(
                    Config.OnBoarding(
                        onBoardingContext = it
                    )
                )
            },
        )

    private fun onboardingComponent(componentContext: ComponentContext, config: Config.OnBoarding): OnBoardingComponent =
        DefaultOnboardingComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            onBoardingContext = config.onBoardingContext,
            onPush = { phoneNumber, isActive, isTermsAccepted ->
                 navigation.push(
                     Config.OTP(
                         onBoardingContext = config.onBoardingContext,
                         phoneNumber = phoneNumber,
                         isActive = isActive,
                         isTermsAccepted = isTermsAccepted
                    )
                 )
            },
        )

    private fun otpComponent(componentContext: ComponentContext, config: Config.OTP): OtpComponent =
        DefaultOtpComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            onBoardingContext = config.onBoardingContext,
            phoneNumber = config.phoneNumber,
            isActive = config.isActive,
            isTermsAccepted = config.isTermsAccepted,
            onValidOTP = { phoneNumber, isTermsAccepted, isActive, onBoardingContext ->
                navigation.push(Config.Auth(
                    phoneNumber = phoneNumber,
                    isTermsAccepted = isTermsAccepted,
                    isActive = isActive,
                    onBoardingContext = onBoardingContext
                ))
            }
        )

    private fun authComponent(componentContext: ComponentContext, config: Config.Auth): AuthComponent =
        DefaultAuthComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            phoneNumber = config.phoneNumber,
            isTermsAccepted = config.isTermsAccepted,
            isActive = config.isActive,
            onBoardingContext = config.onBoardingContext,
            onLogin = {
                // navigate to profile
                navigation.push(Config.RootBottom)
            }
        )

    private fun rootBottomComponent(componentContext: ComponentContext): RootBottomComponent =
        DefaultRootBottomComponent(
            componentContext = componentContext,
            storeFactory = storeFactory
        )

    enum class OnBoardingContext {
        LOGIN,
        REGISTRATION
    }

    private sealed class Config : Parcelable {
        @Parcelize
        object Splash : Config()
        @Parcelize
        data class Welcome (val context: OnBoardingContext) : Config()
        @Parcelize
        data class OnBoarding (val onBoardingContext: OnBoardingContext) : Config()
        @Parcelize
        data class OTP (val onBoardingContext: OnBoardingContext, val phoneNumber: String, val isActive: Boolean, val isTermsAccepted: Boolean) : Config()
        @Parcelize
        data class Auth(val phoneNumber: String, val isTermsAccepted: Boolean, val isActive: Boolean, val onBoardingContext: OnBoardingContext) : Config()
        @Parcelize
        object RootBottom : Config()
    }
}