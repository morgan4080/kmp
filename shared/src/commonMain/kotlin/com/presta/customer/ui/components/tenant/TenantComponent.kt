package com.presta.customer.ui.components.tenant

import com.arkivanov.decompose.value.Value
import com.presta.customer.organisation.Organisation
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.components.tenant.store.TenantStore
import kotlinx.coroutines.flow.StateFlow

interface TenantComponent {

    val tenantStore : TenantStore

    val onBoardingContext: DefaultRootComponent.OnBoardingContext

    val tenantState: StateFlow<TenantStore.State>
    fun onEvent(event: TenantStore.Intent)

    val model: Value<Model>
    fun onSubmitClicked(tenantId: String)
    data class Model(
        val organisation: Organisation
    )
}