package com.presta.customer.ui.components.tenant.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.shortTermLoans.model.PrestaLoanOfferMaturityResponse
import com.presta.customer.network.tenant.model.PrestaTenantResponse

interface TenantStore:
    Store<TenantStore.Intent, TenantStore.State, Nothing> {

    sealed class Intent{
        data  class  GetClientById(val searchTerm: String): Intent()
    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val prestaTenantById: PrestaTenantResponse?=null
    )
}
