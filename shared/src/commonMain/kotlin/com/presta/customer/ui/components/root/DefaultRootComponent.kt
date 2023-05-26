package com.presta.customer.ui.components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.statekeeper.consume
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.ui.components.auth.AuthComponent
import com.presta.customer.ui.components.auth.DefaultAuthComponent
import com.presta.customer.ui.components.onBoarding.DefaultOnboardingComponent
import com.presta.customer.ui.components.onBoarding.OnBoardingComponent
import com.presta.customer.ui.components.otp.DefaultOtpComponent
import com.presta.customer.ui.components.otp.OtpComponent
import com.presta.customer.ui.components.registration.DefaultRegistrationComponent
import com.presta.customer.ui.components.registration.RegistrationComponent
import com.presta.customer.ui.components.rootBottomStack.DefaultRootBottomComponent
import com.presta.customer.ui.components.rootBottomStack.RootBottomComponent
import com.presta.customer.ui.components.splash.DefaultSplashComponent
import com.presta.customer.ui.components.splash.SplashComponent
import com.presta.customer.ui.components.welcome.DefaultWelcomeComponent
import com.presta.customer.ui.components.welcome.WelcomeComponent

class DefaultRootComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
) : RootComponent, ComponentContext by componentContext {

    private var state: State = stateKeeper.consume(key = "AUTH_DATA") ?: State()
    @Parcelize
    private class State(
        var phoneNumber: String = "",
        var memberRefId: String = "",
        var isTermsAccepted: Boolean  = false,
        var isActive: Boolean = false,
        var authenticated: Boolean = false
    ) : Parcelable

    init {
        stateKeeper.register(key = "AUTH_DATA") { state }
    }

    private val navigation = StackNavigation<Config>()

    private fun deliverInitialConfig(): Config {
        return when (state.authenticated) {
            true -> Config.RootBottom
            false -> if (state.phoneNumber !== "") {
                Config.Auth(
                    phoneNumber = state.phoneNumber,
                    isTermsAccepted = state.isTermsAccepted,
                    isActive = state.isActive,
                    onBoardingContext = OnBoardingContext.LOGIN,
                    memberRefId = state.memberRefId
                )
            } else {
                Config.RootBottom
            }
        }
    }

    init {
        println("::::::state.authenticated")
        println(state.authenticated)
        println("::::::state.phoneNumber")
        println(state.phoneNumber)
    }

    private val _childStack =
        childStack(
            source = navigation,
            initialConfiguration = deliverInitialConfig(),
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
            is Config.Register -> RootComponent.Child.RegisterChild(registerComponent(componentContext, config))
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
            onPush = { memberRefId, phoneNumber, isActive, isTermsAccepted, onBoardingContext ->
                 navigation.push(
                     Config.OTP(
                         memberRefId = memberRefId,
                         onBoardingContext = onBoardingContext,
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
            memberRefId = config.memberRefId,
            onBoardingContext = config.onBoardingContext,
            phoneNumber = config.phoneNumber,
            isActive = config.isActive,
            isTermsAccepted = config.isTermsAccepted,
            onValidOTP = { memberRefId, phoneNumber, isTermsAccepted, isActive, onBoardingContext ->
                when(onBoardingContext) {
                    OnBoardingContext.REGISTRATION -> navigation.push(Config.Register(
                        phoneNumber = phoneNumber,
                        isTermsAccepted = isTermsAccepted,
                        isActive = isActive,
                        onBoardingContext = onBoardingContext
                    ))

                    OnBoardingContext.LOGIN -> {
                        if (memberRefId !== null) navigation.push(Config.Auth(
                            memberRefId = memberRefId,
                            phoneNumber = phoneNumber,
                            isTermsAccepted = isTermsAccepted,
                            isActive = isActive,
                            onBoardingContext = onBoardingContext
                        ))
                    }
                }
            }
        )

    private fun registerComponent(componentContext: ComponentContext, config: Config.Register): RegistrationComponent =
        DefaultRegistrationComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            phoneNumber = config.phoneNumber,
            isTermsAccepted = config.isTermsAccepted,
            isActive = config.isActive,
            onBoardingContext = config.onBoardingContext,
            onRegistered = { memberRefId, phoneNumber, isTermsAccepted, isActive, onBoardingContext ->
                navigation.push(Config.Auth(
                    memberRefId = memberRefId,
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
            onLogin = { phoneNumber, isTermsAccepted, isActive ->
                state.authenticated = true
                state.phoneNumber = phoneNumber
                state.isTermsAccepted = isTermsAccepted
                state.isActive = isActive

                navigation.replaceAll(Config.RootBottom)
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
        data class OTP (val memberRefId: String?, val onBoardingContext: OnBoardingContext, val phoneNumber: String, val isActive: Boolean, val isTermsAccepted: Boolean) : Config()
        @Parcelize
        data class Auth(val memberRefId: String, val phoneNumber: String, val isTermsAccepted: Boolean, val isActive: Boolean, val onBoardingContext: OnBoardingContext) : Config()
        @Parcelize
        data class Register(val phoneNumber: String, val isActive: Boolean, val isTermsAccepted: Boolean, val onBoardingContext: OnBoardingContext) : Config()
        @Parcelize
        object RootBottom : Config()
    }
}