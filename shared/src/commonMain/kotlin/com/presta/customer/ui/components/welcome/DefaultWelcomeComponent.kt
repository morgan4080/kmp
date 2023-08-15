package com.presta.customer.ui.components.welcome

import com.presta.customer.MR
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.root.DefaultRootComponent

class DefaultWelcomeComponent (
    componentContext: ComponentContext,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    private val onGetStartedSelected: (onBoardingContext: DefaultRootComponent.OnBoardingContext) -> Unit,
) : WelcomeComponent, ComponentContext by componentContext {
    override val model: Value<WelcomeComponent.Model> =
        MutableValue(WelcomeComponent.Model(
            // create welcome screens here
            items = listOf(
                UserEducationScreens(
                    "Welcome to MobiFlex Easily save, apply and pay for loans anywhere, anytime.",
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

    override fun onGetStarted(onBoardingContext: DefaultRootComponent.OnBoardingContext) {
        println(":::::onBoardingContext:::::::")
        println(onBoardingContext)
        onGetStartedSelected(onBoardingContext)
    }
}

