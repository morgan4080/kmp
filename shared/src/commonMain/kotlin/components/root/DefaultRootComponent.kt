package components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import components.auth.AuthComponent
import components.auth.DefaultAuthComponent
import components.onBoarding.DefaultOnboardingComponent
import components.onBoarding.OnBoardingComponent
import components.otp.DefaultOtpComponent
import components.otp.OtpComponent
import components.rootBottomStack.DefaultRootBottomComponent
import components.rootBottomStack.RootBottomComponent
import components.splash.DefaultSplashComponent
import components.splash.SplashComponent
import components.welcome.DefaultWelcomeComponent
import components.welcome.WelcomeComponent


class DefaultRootComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
) : RootComponent, ComponentContext by componentContext {

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
            is Config.OTP -> RootComponent.Child.OTPChild(otpComponent(componentContext))
            is Config.Auth -> RootComponent.Child.AuthChild(authComponent(componentContext))
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
            onGetStartedSelected = { onBoardingContext ->
                navigation.push(
                    Config.OnBoarding(
                        onBoardingContext = onBoardingContext
                    )
                )
            },
        )

    private fun onboardingComponent(componentContext: ComponentContext, config: Config.OnBoarding): OnBoardingComponent =
        DefaultOnboardingComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            onBoardingContext = config.onBoardingContext,
            onPush = {
                 navigation.push(Config.OTP)
            },
        )

    private fun otpComponent(componentContext: ComponentContext): OtpComponent =
        DefaultOtpComponent(
            componentContext = componentContext,
            onValidOTP = {
                navigation.push(Config.Auth)
            }
        )

    private fun authComponent(componentContext: ComponentContext): AuthComponent =
        DefaultAuthComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            onLogin = {
                // navigate to profile
                navigation.push(Config.RootBottom)
            }
        )

    private fun rootBottomComponent(componentContext: ComponentContext): RootBottomComponent =
        DefaultRootBottomComponent(
            componentContext = componentContext,
        )

    enum class OnBoardingContext {
        LOGIN,
        REGISTRATION
    }

    private sealed class Config : Parcelable {
        @Parcelize
        object Splash : Config()
        @Parcelize
        data class Welcome(val context: OnBoardingContext) : Config()
        @Parcelize
        data class OnBoarding(val onBoardingContext: OnBoardingContext) : Config()
        @Parcelize
        object OTP : Config()
        @Parcelize
        object Auth : Config()
        @Parcelize
        object RootBottom : Config()
    }
}