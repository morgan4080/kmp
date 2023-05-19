package components.splash

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.presta.customer.MR
import organisation.Organisation
import organisation.OrganisationModel

class DefaultSplashComponent(
    componentContext: ComponentContext,
    private val onSignUp: () -> Unit,
    private val onSignIn: () -> Unit,
): SplashComponent, ComponentContext by componentContext {
    override val model: Value<SplashComponent.Model> =
        MutableValue(SplashComponent.Model(
            organisation = OrganisationModel.organisation
        ))

    override fun onSignUpClicked() {
        onSignUp()
    }
    override fun onSignInClicked() {
        onSignIn()
    }
}