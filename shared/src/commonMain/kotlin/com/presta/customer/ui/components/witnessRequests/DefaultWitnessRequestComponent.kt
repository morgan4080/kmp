package com.presta.customer.ui.components.witnessRequests

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStoreFactory
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DefaultWitnessRequestComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit,
    private val onAcceptClicked: (
        loanNumber: String,
        amount: Double,
        loanRequestRefId: String,
        memberRefId: String,
        witnessRefId: String
    ) -> Unit
) : WitnessRequestComponent, ComponentContext by componentContext {

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override val authStore: AuthStore =
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

    override val applyLongTermLoansStore: ApplyLongTermLoansStore =
        instanceKeeper.getStore {
            ApplyLongTermLoansStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State> =
        applyLongTermLoansStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: ApplyLongTermLoansStore.Intent) {
        applyLongTermLoansStore.accept(event)
    }

    override val sigHomeStore: SignHomeStore =
        instanceKeeper.getStore {
            SignHomeStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val signHomeState: StateFlow<SignHomeStore.State> =
        sigHomeStore.stateFlow

    override fun onProfileEvent(event: SignHomeStore.Intent) {
        sigHomeStore.accept(event)
    }

    private var authUserScopeJob: Job? = null
    private fun checkAuthenticatedUser() {
        if (authUserScopeJob?.isActive == true) return

        authUserScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(
                        AuthStore.Intent.CheckAuthenticatedUser(
                            token = state.cachedMemberData.accessToken,
                            state.cachedMemberData.refId
                        )
                    )
                    onProfileEvent(
                        SignHomeStore.Intent.GetPrestaTenantByPhoneNumber(
                            token = state.cachedMemberData.accessToken,
                            phoneNumber = state.cachedMemberData.phoneNumber
                        )
                    )
                    this.cancel()
                }
            }
        }
    }

    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onProductSelected() {
        onProductClicked()
    }

    override fun onAcceptSelected(
        loanNumber: String,
        amount: Double,
        loanRequestRefId: String,
        memberRefId: String,
        witnessRefId: String
    ) {
        onAcceptClicked(
            loanNumber,
            amount,
            loanRequestRefId,
            memberRefId,
            witnessRefId
        )
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }
}