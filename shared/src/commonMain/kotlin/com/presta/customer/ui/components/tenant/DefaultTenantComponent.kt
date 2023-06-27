package com.presta.customer.ui.components.tenant

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.components.tenant.store.TenantStore
import com.presta.customer.ui.components.tenant.store.TenantStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)

class DefaultTenantComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onSubmit: (onBoardingContext: DefaultRootComponent.OnBoardingContext, tenantId: String) -> Unit,

): TenantComponent, ComponentContext by componentContext {
    private val scope = coroutineScope(mainContext + SupervisorJob())


    override val model: Value<TenantComponent.Model> =
        MutableValue(
            TenantComponent.Model(
            organisation = OrganisationModel.organisation
        ))

    override val tenantStore: TenantStore=
        instanceKeeper.getStore {
             TenantStoreFactory(
                 storeFactory = storeFactory,
                 componentContext = componentContext,
             ) .create()
        }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val tenantState: StateFlow<TenantStore.State> = tenantStore.stateFlow

    override fun onEvent(event: TenantStore.Intent) {
        tenantStore.accept(event)
    }

    override fun onSubmitClicked() {
       //onSubmit()
    }

}