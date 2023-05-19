package components.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import components.auth.store.AuthStore
import components.auth.store.AuthStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import organisation.OrganisationModel

class DefaultAuthComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val onLogin: () -> Unit,
): AuthComponent, ComponentContext by componentContext {

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<AuthStore.State> = authStore.stateFlow

    init {
        onEvent(AuthStore.Intent.AuthenticateClient(
           client_secret = OrganisationModel.organisation.client_secret
        ))
    }

    // authenticate client to get token
    // token gives access to member api, member api will have pin status and terms status
    // token access allows verified user to set pin on an account if terms not accepted or pinStatus != SET

    // EVENTS
    // - AuthenticateClient
    // - GetMemberDetails
    // - UpdateMember
    // - LoginUser

    override val models =
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

    override fun onEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }
}