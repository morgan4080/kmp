package com.presta.customer.ui.components.shortTermLoans

import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import kotlinx.coroutines.flow.StateFlow

interface ShortTermLoansComponent {


    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    val shortTermloansStore: ShortTermLoansStore

    val shortTermloansState: StateFlow<ShortTermLoansStore.State>

    val model: Value<Model>

    fun onSelected(refId: String)
    fun onSelecte2(refId: String)
    fun onConfirmSelected(refId: String)

    fun onBackNav()


    data class Model(
        val items: List<String>,
    )

}