package com.presta.customer.ui.components.signAppHome.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.signHome.model.Details
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse

interface SignHomeStore : Store<SignHomeStore.Intent, SignHomeStore.State, Nothing> {
    sealed class Intent {
        data class GetPrestaTenantByPhoneNumber(val token: String, val phoneNumber: String) :
            Intent()

        data class GetPrestaTenantByMemberNumber(val token: String, val memberNumber: String) :
            Intent()

        data class UpdatePrestaTenantDetails(
            val token: String,
            val memberRefId: String,
            val details: Details
        ) : Intent()

    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val prestaTenantByPhoneNumber: PrestaSignUserDetailsResponse? = null,
        val prestaTenantByMemberNumber: PrestaSignUserDetailsResponse? = null,
        val updatePrestaTenantDetails: PrestaSignUserDetailsResponse? = null
    )
}