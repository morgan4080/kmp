package com.presta.customer.ui.components.shortTermLoans

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStoreFactory
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

class DefaultShortTernLoansComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    storeFactory: StoreFactory,
    private val onProductSelected: (refId: String) -> Unit,
    private val onProduct2Selected: (refId: String) -> Unit,
    private val onConfirmClicked: (refId: String) -> Unit,
    private val onBackNavClicked: () -> Unit,

    ): ShortTermLoansComponent, ComponentContext by componentContext {

    private val models = MutableValue(
        ShortTermLoansComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<ShortTermLoansComponent.Model> = models

    override fun onSelected(refId: String) {
        onProductSelected(refId)
    }

    override fun onSelecte2(refId: String) {
       onProduct2Selected(refId)
    }

    override fun onConfirmSelected(refId: String) {
       onConfirmClicked(refId)
    }

    override fun onBackNav() {
       onBackNavClicked()
    }



    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: ShortTermLoansStore.Intent) {

        TODO("Not yet implemented")
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())


    override val authStore: AuthStore=
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = PinStatus.SET
            ).create()
        }

    override val authState: StateFlow<AuthStore.State> =authStore.stateFlow

    override val shortTermloansStore: ShortTermLoansStore=
        instanceKeeper.getStore {
            ShortTermLoansStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val shortTermloansState: StateFlow<ShortTermLoansStore.State> = shortTermloansStore.stateFlow

}