package com.presta.customer.ui.components.profile

import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.auth.store.AuthStore
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {
    val model: Value<Model>

    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>
    fun onProfileSelected()

    fun onAuthEvent(event: AuthStore.Intent)

    data class Model(
        val items: List<String>,
    )
}