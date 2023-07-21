package com.presta.customer.ui.components.signAppHome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DefaultSignHomeComponent (
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onApplyLoanClicked: () -> Unit,
    private val onGuarantorshipRequestsClicked: () -> Unit,
    private val onFavouriteGuarantorsClicked: () -> Unit,
    private val witnessRequestClicked: () -> Unit,
): SignHomeComponent, ComponentContext by componentContext {
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

    override val sigHomeStore: SignHomeStore =
        instanceKeeper.getStore {
            SignHomeStoreFactory(
                storeFactory = storeFactory
            ).create()
        }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val  signHomeState: StateFlow<SignHomeStore.State> =
        sigHomeStore.stateFlow

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
                            token = state.cachedMemberData.accessToken
                        )
                    )
                    onEvent(
                        SignHomeStore.Intent.GetPrestaTenantByPhoneNumber(
                            token = state.cachedMemberData.accessToken,
                            phoneNumber = state.cachedMemberData.phoneNumber
                        )
                    )
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


    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }
}