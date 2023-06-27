package com.presta.customer.ui.components.savings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.components.savings.store.SavingsStore
import com.presta.customer.ui.components.savings.store.SavingsStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
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

class DefaultSavingsComponent (
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    storeFactory: StoreFactory,
    private val onPop: () -> Unit,
    private val onAddSavingsClicked: (sharePrice: Double) -> Unit,
    private val onSeeAlClicked: () -> Unit,
): SavingsComponent, ComponentContext by componentContext {

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

    override val savingsStore =
        instanceKeeper.getStore {
            SavingsStoreFactory(
                storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val savingsState: StateFlow<SavingsStore.State> = savingsStore.stateFlow


    override fun onEvent(event: SavingsStore.Intent) {
        savingsStore.accept(event)
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private var refreshTokenScopeJob: Job? = null

    private fun refreshToken() {
        if (refreshTokenScopeJob?.isActive == true) return

        refreshTokenScopeJob = scope.launch {
            authState.collect { state ->
                if (OrganisationModel.organisation.tenant_id!=null){
                    if (state.cachedMemberData !== null) {
                        onAuthEvent(AuthStore.Intent.RefreshToken(
                            tenantId = OrganisationModel.organisation.tenant_id!!,
                            refId = state.cachedMemberData.refId
                        ))
                    }
                }
                this.cancel()
            }
        }
    }

    private var fetchSavingsDataJob: Job? = null

    private fun fetchSavingsData (access_token: String, refId: String) {
        if (fetchSavingsDataJob?.isActive == true) return

        fetchSavingsDataJob = scope.launch {
            savingsState.collect { state ->
                if (state.transactionMapping !== null) {
                    onEvent(
                        SavingsStore.Intent.GetSavingsTransactions (
                            token = access_token,
                            refId = refId,
                            purposeIds = listOf("2","3"),
                            searchTerm = null
                        )
                    )
                }
            }
        }
    }

    private var loadEssentialsJob: Job? = null

    override fun loadEssentials() {
        if (loadEssentialsJob?.isActive == true) return

        loadEssentialsJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.CheckAuthenticatedUser(
                        token = state.cachedMemberData.accessToken
                    ))

                    onEvent(
                        SavingsStore.Intent.GetSavingsBalances (
                            token = state.cachedMemberData.accessToken,
                            refId = state.cachedMemberData.refId,
                        )
                    )

                    onEvent(
                        SavingsStore.Intent.GetTransactionsMapping (
                            token = state.cachedMemberData.accessToken
                        )
                    )

                    fetchSavingsData(state.cachedMemberData.accessToken, state.cachedMemberData.refId)
                }
            }
        }
    }

    override fun onBack() {
        onPop()
    }
    override fun onAddSavingsSelected(sharePrice: Double) {
       onAddSavingsClicked(sharePrice)
    }

    override fun onSeeALlSelected() {
       onSeeAlClicked()
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        refreshToken()
        loadEssentials()
    }
}