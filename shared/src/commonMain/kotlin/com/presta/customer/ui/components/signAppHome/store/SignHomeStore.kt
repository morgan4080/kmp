package com.presta.customer.ui.components.signAppHome.store

import com.arkivanov.mvikotlin.core.store.Store
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
            val details: MutableMap<String, String>
        ) : Intent()
        data class UpdatePrestaTenantPersonalInfo(
            val token: String,
            val memberRefId: String,
            val firstName: String,
            val lastName: String,
            val phoneNumber: String,
            val idNumber: String,
            val email: String,
        ) : Intent()

    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val prestaTenantByPhoneNumber: PrestaSignUserDetailsResponse? = null,
        var prestaTenantByMemberNumber: PrestaSignUserDetailsResponse? = null,
        val updatePrestaTenantDetails: PrestaSignUserDetailsResponse? = null,
        val updatePrestaTenantPersonalInfo: PrestaSignUserDetailsResponse? = null
    )
}