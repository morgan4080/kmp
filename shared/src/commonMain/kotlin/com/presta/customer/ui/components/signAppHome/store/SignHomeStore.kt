package com.presta.customer.ui.components.signAppHome.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse

interface SignHomeStore : Store<SignHomeStore.Intent, SignHomeStore.State, Nothing> {

    sealed class Intent {
        data class GetPrestaTenantByPhoneNumber(val token: String, val phoneNumber: String) : Intent()
    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val prestaTenantById: PrestaSignUserDetailsResponse? = null
    )
}