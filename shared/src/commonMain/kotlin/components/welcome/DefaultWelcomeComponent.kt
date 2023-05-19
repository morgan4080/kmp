package components.welcome

import com.presta.customer.MR
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import components.countries.Country
import components.root.DefaultRootComponent
import organisation.OrganisationModel

class DefaultWelcomeComponent (
    componentContext: ComponentContext,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    private val onGetStartedSelected: (country: Country, onBoardingContext: DefaultRootComponent.OnBoardingContext) -> Unit,
) : WelcomeComponent, ComponentContext by componentContext {
    override val model: Value<WelcomeComponent.Model> =
        MutableValue(WelcomeComponent.Model(
            // create welcome screens here
            items = listOf(
                UserEducationScreens(
                    "Welcome to ${OrganisationModel.organisation.tenant_name}. Easily save, apply and pay for loans anywhere, anytime.",
                    "Instant loans & 24/7 access to your account",
                    MR.images.send_money_abroad_dark,
                    MR.images.send_money_abroad_light,
                ),
                UserEducationScreens(
                    "Approve and Sign loan documents electronically on your mobile device.",
                    "Embrace the future with digital guarantorship",
                    MR.images.trust_dark,
                    MR.images.trust_light,
                ),
                UserEducationScreens(
                    "Bank grade security to keep your transactions and data secure.",
                    "Ultimate experience. Secure, convenient in one app.",
                    MR.images.receive_money_dark,
                    MR.images.receive_money_light,
                )
            ),
            onBoardingContext = onBoardingContext
        )
    )

    override fun onGetStarted(country: Country, onBoardingContext: DefaultRootComponent.OnBoardingContext) {
        onGetStartedSelected(country, onBoardingContext)
    }
}

