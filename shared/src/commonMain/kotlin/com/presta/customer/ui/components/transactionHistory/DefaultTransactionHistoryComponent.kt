package com.presta.customer.ui.components.transactionHistory

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
import com.presta.customer.ui.components.transactionHistory.store.TransactionHistoryStore
import com.presta.customer.ui.components.transactionHistory.store.TransactionHistoryStoreFactory
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

class DefaultTransactionHistoryComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    storeFactory: StoreFactory,
    private val onPop: () -> Unit
) : TransactionHistoryComponent, ComponentContext by componentContext {
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

    override val transactionHistoryStore =
        instanceKeeper.getStore {
            TransactionHistoryStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val transactionHistoryState: StateFlow<TransactionHistoryStore.State> = transactionHistoryStore.stateFlow

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun onEvent(event: TransactionHistoryStore.Intent) {
        transactionHistoryStore.accept(event)
    }

    override fun onBack() {
        onPop()
    }

    private var refreshTokenScopeJob: Job? = null

    private fun refreshToken() {
        if (refreshTokenScopeJob?.isActive == true) return

        refreshTokenScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.RefreshToken(
                        tenantId = OrganisationModel.organisation.tenant_id,
                        refId = state.cachedMemberData.refId
                    ))
                }
                this.cancel()
            }
        }
    }

    private var profileStateScopeJob: Job? = null

    private fun fetchAllTransactionHistory (access_token: String, refId: String) {
        if (profileStateScopeJob?.isActive == true) return

        profileStateScopeJob = scope.launch {
            transactionHistoryState.collect { state ->
                if (state.transactionMapping !== null) {
                    onEvent(TransactionHistoryStore.Intent.GetTransactionHistory (
                        token = access_token,
                        refId = refId,
                        purposeIds = state.transactionMapping.keys.toList(),
                        searchTerm = null
                    ))
                }
            }
        }
    }

    private var authUserScopeJob: Job? = null

    override fun checkAuthenticatedUser() {
        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.CheckAuthenticatedUser(
                        token = state.cachedMemberData.accessToken,
                        state.cachedMemberData.refId
                    ))

                    onEvent(TransactionHistoryStore.Intent.GetTransactionMapping (
                        token = state.cachedMemberData.accessToken
                    ))

                    fetchAllTransactionHistory(state.cachedMemberData.accessToken, state.cachedMemberData.refId)
                }
            }
        }
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)

        checkAuthenticatedUser()

        refreshToken()
    }

    private var fetchMappingJob: Job? = null

    override fun onMappingChange(mapping: List<String>) {

        fetchMappingJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onEvent(
                        TransactionHistoryStore.Intent.GetTransactionHistory(
                            token = state.cachedMemberData.accessToken,
                            refId = state.cachedMemberData.refId,
                            purposeIds = mapping,
                            searchTerm = null
                        )
                    )
                }
            }
        }
    }
}