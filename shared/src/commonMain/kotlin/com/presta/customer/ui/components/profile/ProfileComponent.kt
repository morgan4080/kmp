package com.presta.customer.ui.components.profile

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.profile.store.ProfileStore
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    val profileStore: ProfileStore

    val profileState: StateFlow<ProfileStore.State>

    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ProfileStore.Intent)
    fun seeAllTransactions()

    fun goToSavings()
    fun goToLoans()
    fun goToPayLoans()
    fun goToStatement()
}