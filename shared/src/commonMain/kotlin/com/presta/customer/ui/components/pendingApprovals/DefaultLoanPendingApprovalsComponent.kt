package com.presta.customer.ui.components.pendingApprovals

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.loanRequest.model.LoanApplicationStatus
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStoreFactory
import kotlinx.coroutines.CoroutineDispatcher
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
class DefaultLoanPendingApprovalsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineDispatcher,
    private val onBack: () -> Unit,
): LoanPendingApprovalsComponent, ComponentContext by componentContext {
    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = PinStatus.SET
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val modeOfDisbursementStore =
        instanceKeeper.getStore {
            ModeOfDisbursementStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State> =
        modeOfDisbursementStore.stateFlow

    override fun onBackClicked() {
        onBack()
    }
    override fun onRequestLoanEvent(event: ModeOfDisbursementStore.Intent) {
        modeOfDisbursementStore.accept(event)
    }
    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }
    private val scope = coroutineScope(mainContext + SupervisorJob())
    override fun checkAuthenticatedUser() {
        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.CheckAuthenticatedUser(
                        token = state.cachedMemberData.accessToken,
                        state.cachedMemberData.refId
                    ))

                    onRequestLoanEvent(ModeOfDisbursementStore.Intent.GetPendingApprovals(
                        token = state.cachedMemberData.accessToken,
                        customerRefId = state.cachedMemberData.refId,
                        applicationStatus = listOf(LoanApplicationStatus.NEWAPPLICATION)
                    ))

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