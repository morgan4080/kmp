package components.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultAuthComponent(
    componentContext: ComponentContext,
    termsAccepted: Boolean,
    pinConfirmed: Boolean,
    pinCreated: Boolean,
    private val onPinSet: () -> Unit,
    private val onLogin: () -> Unit,
): AuthComponent, ComponentContext by componentContext {

    override val model: Value<AuthComponent.Model> =
        MutableValue(
            AuthComponent.Model(
                inputs = listOf(
                    AuthComponent.InputMethod(
                        value = ""
                    ),
                    AuthComponent.InputMethod(
                        value = ""
                    ),
                    AuthComponent.InputMethod(
                        value = ""
                    ),
                    AuthComponent.InputMethod(
                        value = ""
                    )
                ),
                /*"Login to Presta Customer using the following pin code"*/
                label = "You'll be able to login to Presta Customer using the following pin code",
                /*if (termsAccepted)
                    "Enter pin code"
                else if (pinConfirmed)
                    "Enter pin code"
                else if (pinCreated)
                    "Confirm pin code"
                else
                    "Create pin code",*/
                title = "Create pin code",
                phone_number = null,
                email = null,
                tenant_id = null,
                termsAccepted = termsAccepted,
                pinConfirmed = pinConfirmed,
                pinCreated = pinCreated,
                context = Contexts.CREATE_PIN

            )
        )
    override fun onPinSubmit() {
        println("onPinSubmit")
        onPinSet()
    }
    override fun onConfirmation() {
        println("onConfirmation")
        onLogin()
    }
    override fun onLoginSubmit() {
        println("onLoginSubmit")
        onLogin()
    }
}