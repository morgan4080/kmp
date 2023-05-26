package com.presta.customer.ui.components.profile.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.profile.model.PrestaBalancesResponse

interface ProfileStore: Store<ProfileStore.Intent, ProfileStore.State, Nothing> {

    sealed class Intent{
        data class GetBalances(val token: String, val refId: String): Intent()
    }


    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val balances: PrestaBalancesResponse? = null
    )
}