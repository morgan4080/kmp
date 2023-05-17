package components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import components.auth.AuthComponent
import components.auth.DefaultAuthComponent
import components.countries.CountriesComponent
import components.countries.DefaultCountriesComponent
import components.onBoarding.DefaultOnboardingComponent
import components.onBoarding.OnBoardingComponent
import components.otp.DefaultOtpComponent
import components.otp.OtpComponent
import components.rootBottomStack.DefaultRootBottomComponent
import components.rootBottomStack.RootBottomComponent
import components.welcome.DefaultWelcomeComponent
import components.welcome.WelcomeComponent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class DefaultRootComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val _childStack =
        childStack(
            source = navigation,
            initialConfiguration = Config.Welcome,
            handleBackButton = true,
            childFactory = ::createChild,
            key = "onboardingStack"
        )

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = _childStack

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Welcome -> RootComponent.Child.WelcomeChild(welcomeComponent(componentContext))
            is Config.Onboarding -> RootComponent.Child.OnboardingChild(onboardingComponent(componentContext, config))
            is Config.Countries -> RootComponent.Child.CountriesChild(countriesComponent(componentContext))
            is Config.OTP -> RootComponent.Child.OTPChild(otpComponent(componentContext))
            is Config.Auth -> RootComponent.Child.AuthChild(authComponent(componentContext))
            is Config.RootBottom -> RootComponent.Child.RootBottomChild(rootBottomComponent(componentContext))
        }

    private fun welcomeComponent(componentContext: ComponentContext): WelcomeComponent =
        DefaultWelcomeComponent(
            componentContext = componentContext,
            onGetStartedSelected = {
                navigation.push(Config.Onboarding(country = Json.encodeToString(it)))
            },
        )

    private fun onboardingComponent(componentContext: ComponentContext, config: Config.Onboarding): OnBoardingComponent =
        DefaultOnboardingComponent(
            componentContext = componentContext,
            country = config.country,
            onSubmitClicked = {
                 navigation.push(Config.OTP)
            },
            onSelectCountryClicked = {
                navigation.push(Config.Countries)
            },
            onSelectOrganisationClicked = {
                // navigation.push(Config.Organisations)
            }
        )

    private fun countriesComponent(componentContext: ComponentContext): CountriesComponent =
        DefaultCountriesComponent(
            componentContext = componentContext,
            onSelectedCountry = { country ->
                navigation.pop {
                    (childStack.value.active.instance as? OnBoardingComponent)?.onCountrySelected(country = country)
                }
            },
            onBackClicked = {
                navigation.pop()
            }
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

    private sealed class Config : Parcelable {
        @Parcelize
        object Welcome : Config()
        @Parcelize
        data class Onboarding(val country: String) : Config()
        @Parcelize
        object Countries : Config()
        @Parcelize
        object OTP : Config()
        @Parcelize
        object Auth : Config()
        @Parcelize
        object RootBottom : Config()
    }
}