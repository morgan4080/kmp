package com.presta.customer.ui.components.splash

import com.arkivanov.decompose.value.Value
import com.presta.customer.organisation.Organisation
import com.presta.customer.ui.components.auth.store.AuthStore
import kotlinx.coroutines.flow.StateFlow

interface SplashComponent {
    val model: Value<Model>
    fun onSignUpClicked()
    fun onSignInClicked()
    data class Model(
        val organisation: Organisation
    )

    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    fun onEvent(event: AuthStore.Intent)
}