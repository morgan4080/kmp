package com.presta.customer.ui.components.signAppHome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.Platform
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

class DefaultSignHomeComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onApplyLoanClicked: () -> Unit,
    private val onGuarantorshipRequestsClicked: () -> Unit,
    private val onFavouriteGuarantorsClicked: () -> Unit,
    private val witnessRequestClicked: () -> Unit,
    private val onGotoLoanRequestsClicked: (refId: String) -> Unit,
    private val logoutToSplash: () -> Unit,
) : SignHomeComponent, ComponentContext by componentContext, KoinComponent {
    override val platform by inject<Platform>()
    private val scope = coroutineScope(mainContext + SupervisorJob())
    override val authStore: AuthStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = PinStatus.SET,
                onLogOut = logoutToSplash
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val sigHomeStore: SignHomeStore =
        instanceKeeper.getStore {
            SignHomeStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val signHomeState: StateFlow<SignHomeStore.State> =
        sigHomeStore.stateFlow

    override val applyLongTermLoansStore: ApplyLongTermLoansStore =
        instanceKeeper.getStore {
            ApplyLongTermLoansStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State> =
        applyLongTermLoansStore.stateFlow

    override fun onApplyLongTermLoanEvent(event: ApplyLongTermLoansStore.Intent) {
        applyLongTermLoansStore.accept(event)
    }

    override fun goToLoanRequests(refId: String) {
        onGotoLoanRequestsClicked(refId)
    }

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: SignHomeStore.Intent) {
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
                    onEvent(
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

    override fun onApplyLoanSelected() {
        onApplyLoanClicked()
    }

    override fun guarantorshipRequestsSelected() {
        onGuarantorshipRequestsClicked()
    }

    override fun favouriteGuarantorsSelected() {
        onFavouriteGuarantorsClicked()
    }

    override fun witnessRequestSelected() {
        witnessRequestClicked()
    }
    override fun logout() {
        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.LogOutUser)
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