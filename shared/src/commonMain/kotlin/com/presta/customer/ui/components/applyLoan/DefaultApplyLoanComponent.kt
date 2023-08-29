package com.presta.customer.ui.components.applyLoan

import com.arkivanov.decompose.ComponentContext
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)

class DefaultApplyLoanComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    storeFactory: StoreFactory,
    private val onShortTermClicked: () -> Unit,
    private val onLongTermClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,
    private val onPop: () -> Unit
): ApplyLoanComponent, ComponentContext by componentContext {

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = PinStatus.SET,
                onLogOut = {

                }
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onShortTermSelected() {
        onShortTermClicked()
    }

    override fun onLongTermSelected() {
        onLongTermClicked()
    }

    override fun onBackNavSelected() {
     onBackNavClicked()
    }

    override fun onBack() {
       onPop()
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private fun checkAuthenticatedUser() {

        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.CheckAuthenticatedUser(
                        token = state.cachedMemberData.accessToken
                    ))

                    onAuthEvent(
                        AuthStore.Intent.CheckServices(
                            token = state.cachedMemberData.accessToken,
                            tenantId = state.cachedMemberData.tenantId
                        )
                    )

                    onAuthEvent(
                        AuthStore.Intent.CheckServiceConfigs(
                            token = state.cachedMemberData.accessToken,
                            tenantId = state.cachedMemberData.tenantId
                        )
                    )

                    this.cancel()
                }
            }
        }
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }

}