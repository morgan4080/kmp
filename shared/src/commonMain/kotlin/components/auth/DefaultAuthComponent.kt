package components.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import components.auth.store.AuthStore
import components.auth.store.AuthStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultAuthComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val onLogin: () -> Unit,
): AuthComponent, ComponentContext by componentContext {

    init {
        // check if termsAccepted on api
        // switch model context to
    }

    private val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<AuthStore.State> = authStore.stateFlow

    private val models =
        MutableValue(
            AuthComponent.Model(
                loading = false,
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
                label = "You'll be able to login to Presta Customer using the following pin code",
                title = "Create pin code",
                phone_number = null,
                email = null,
                tenant_id = null,
                termsAccepted = false,
                pinConfirmed = false,
                pinCreated = false,
                context = Contexts.CREATE_PIN
            )
        )

    override val model : Value<AuthComponent.Model> = models
    override fun onPinSubmit() {
        println("onPinSubmit")
        models.update {
            it.copy(
                title = "Confirm pin code",
                pinCreated = true,
                context = Contexts.CONFIRM_PIN
            )
        }
    }
    override fun onConfirmation() {
        println("onConfirmation")
        models.update {
            it.copy(
                label = "Login to Presta Customer using the following pin code",
                title = "Enter pin code",
                pinConfirmed = true,
                termsAccepted = true,
                context = Contexts.LOGIN
            )
        }
        onLogin()
    }
    override fun onLoginSubmit() {
        println("onLoginSubmit")
        onLogin()
    }
}