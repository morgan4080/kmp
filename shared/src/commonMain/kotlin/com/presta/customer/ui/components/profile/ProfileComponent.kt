package com.presta.customer.ui.components.profile

import com.presta.customer.ui.components.addSavings.store.AddSavingsStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.profile.store.ProfileStore
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    val profileStore: ProfileStore
    val profileState: StateFlow<ProfileStore.State>
    val addSavingsStore: AddSavingsStore
    val addSavingsState: StateFlow<AddSavingsStore.State>
    fun onAddSavingsEvent(event: AddSavingsStore.Intent)
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ProfileStore.Intent)
    fun seeAllTransactions()
    fun goToSavings()
    fun goToLoans()
    fun goToPayLoans()
    fun goToStatement()
    fun logout()
    fun reloadModels()
    fun activateAccount(amount: Double)
}