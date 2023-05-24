package com.presta.customer.ui.components.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultProfileComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val onProfileClicked: () -> Unit
) : ProfileComponent, ComponentContext by componentContext {

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

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    init {
        // take user token

        onAuthEvent(AuthStore.Intent.CheckAuthenticatedUser(
            token = ""
        ))
    }

    override val model: Value<ProfileComponent.Model> =
        MutableValue(
            ProfileComponent.Model(
                items = List(120) { "Profile $it" }
            )
        )

    override fun onProfileSelected() {
        onProfileClicked()
    }
}